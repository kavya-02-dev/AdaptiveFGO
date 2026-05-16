package com.adaptivefgo.navigator.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.hardware.*
import android.location.*
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import com.adaptivefgo.navigator.BuildConfig
import com.adaptivefgo.navigator.R
import com.adaptivefgo.navigator.algorithms.*
import com.adaptivefgo.navigator.cloud.CloudSyncModule
import com.adaptivefgo.navigator.data.*
import com.adaptivefgo.navigator.repository.TrajectoryRepository
import kotlinx.coroutines.*
import kotlin.math.*

/**
 * LocalizationService - Core Background Processing System
 *
 * - Runs as Android Foreground Service
 * - Collects ALL sensors centrally (one pipeline)
 * - Distributes data to all 5 algorithms simultaneously
 * - Stores results in TrajectoryRepository
 * - UI reads repository; no algorithm code in UI
 */
class LocalizationService : Service(), SensorEventListener, LocationListener {

    companion object {
        const val CHANNEL_ID = "localization_channel"
        const val NOTIFICATION_ID = 1001
        const val TAG = "LocalizationService"

        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_RESET = "ACTION_RESET"
    }

    // ─── Sensors ─────────────────────────────────────────────────────────────
    private lateinit var sensorManager: SensorManager
    private lateinit var locationManager: LocationManager
    private var accelSensor: Sensor? = null
    private var gyroSensor: Sensor? = null
    private var magnetometerSensor: Sensor? = null
    private var stepDetectorSensor: Sensor? = null

    // ─── Raw sensor buffers ──────────────────────────────────────────────────
    @Volatile private var accel = FloatArray(3)
    @Volatile private var gyro = FloatArray(3)
    @Volatile private var mag = FloatArray(3)
    @Volatile private var stepDetected = false
    @Volatile private var lastGnssData = GnssData()
    @Volatile private var heading = 0.0

    // Orientation matrices
    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)
    private val accelBuffer = ArrayDeque<Float>(50)

    // ─── Algorithms ──────────────────────────────────────────────────────────
    private val gnssProcessor = GnssProcessor()
    private val pdrProcessor = PdrProcessor()
    private val kalmanProcessor = KalmanFilterProcessor()
    private val fixedLagProcessor = FixedLagFgoProcessor()
    private val adaptiveFgoProcessor = AdaptiveFgoProcessor()
    private val cloudSync = CloudSyncModule()

    // ─── Processing coroutine ────────────────────────────────────────────────
    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var processingJob: Job? = null

    // GNS satellite info
    private var satelliteCount = 0
    private val cn0Values = mutableListOf<Float>()

    // Step length & variance
    private var stepLength = 0.65
    private var motionVariance = 0.0

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        initSensors()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                startForeground(NOTIFICATION_ID, buildNotification())
                startSensors()
                startProcessingLoop()
                TrajectoryRepository.setServiceRunning(true)
                Log.i(TAG, "Service started")
            }
            ACTION_STOP -> {
                stopSelf()
            }
            ACTION_RESET -> {
                resetAll()
            }
        }
        return START_STICKY
    }

    private fun startProcessingLoop() {
        processingJob = serviceScope.launch {
            while (isActive) {
                delay(100)  // 10 Hz processing rate
                processSnapshot()
            }
        }
    }

    // ─── Central Sensor Snapshot Creation ────────────────────────────────────

    private fun processSnapshot() {
        // Compute heading from accel + mag
        if (SensorManager.getRotationMatrix(rotationMatrix, null, accel, mag)) {
            SensorManager.getOrientation(rotationMatrix, orientationAngles)
            heading = Math.toDegrees(orientationAngles[0].toDouble())
                .let { if (it < 0) it + 360.0 else it }
        }

        // Compute motion variance from accelerometer history
        motionVariance = computeMotionVariance()

        // Step length estimation
        val accMag = sqrt(accel[0] * accel[0] + accel[1] * accel[1] + accel[2] * accel[2]).toDouble()
        stepLength = estimateStepLength(accMag)

        // Build central snapshot (single object, distributed to all algorithms)
        val snapshot = SensorSnapshot(
            timestamp = System.currentTimeMillis(),
            gnss = lastGnssData.copy(
                satelliteCount = satelliteCount,
                cn0Values = cn0Values.toList()
            ),
            imu = ImuData(
                timestamp = System.currentTimeMillis(),
                ax = accel[0], ay = accel[1], az = accel[2],
                gx = gyro[0], gy = gyro[1], gz = gyro[2],
                mx = mag[0], my = mag[1], mz = mag[2]
            ),
            heading = heading,
            stepDetected = stepDetected,
            stepLength = stepLength,
            motionVariance = motionVariance,
            headingUncertainty = computeHeadingUncertainty()
        )

        // Reset step flag after reading
        stepDetected = false

        // Update repository with latest snapshot
        TrajectoryRepository.updateLatestSnapshot(snapshot)

        // ── Distribute to all algorithms simultaneously ───────────────────────
        gnssProcessor.process(snapshot)
        pdrProcessor.process(snapshot)
        val pdrState = pdrProcessor.getCurrentState()
        kalmanProcessor.process(snapshot, pdrState)
        fixedLagProcessor.process(snapshot, pdrState)
        adaptiveFgoProcessor.process(snapshot, pdrState)

        // Cloud sync for batch FGO
        if (snapshot.gnss.isValid || snapshot.stepDetected) {
            val factor = FgoFactor(
                timestamp = snapshot.timestamp,
                gnssLat = snapshot.gnss.latitude,
                gnssLon = snapshot.gnss.longitude,
                gnssNoise = snapshot.gnss.accuracy.toDouble().coerceAtLeast(1.0),
                pdrDeltaLat = if (snapshot.stepDetected) snapshot.gnss.latitude - pdrState.latitude else 0.0,
                pdrDeltaLon = if (snapshot.stepDetected) snapshot.gnss.longitude - pdrState.longitude else 0.0,
                pdrNoise = 0.5
            )
            cloudSync.addFactor(factor)
            cloudSync.maybeSync(BuildConfig.GCP_BATCH_FGO_URL)
        }

        // Update notification
        updateNotification(snapshot)
    }

    // ─── Sensor Listeners ────────────────────────────────────────────────────

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                accel = event.values.copyOf()
                accelBuffer.add(sqrt(event.values[0].pow(2) + event.values[1].pow(2) + event.values[2].pow(2)))
                if (accelBuffer.size > 50) accelBuffer.removeFirst()
            }
            Sensor.TYPE_GYROSCOPE -> {
                gyro = event.values.copyOf()
            }
            Sensor.TYPE_MAGNETIC_FIELD -> {
                mag = event.values.copyOf()
            }
            Sensor.TYPE_STEP_DETECTOR -> {
                stepDetected = true
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onLocationChanged(location: Location) {
        lastGnssData = GnssData(
            timestamp = location.time,
            latitude = location.latitude,
            longitude = location.longitude,
            accuracy = location.accuracy,
            speed = location.speed,
            isValid = location.accuracy < 100f
        )
    }

    // GNSS satellite status listener
    private val gnssStatusCallback = object : GnssStatus.Callback() {
        override fun onSatelliteStatusChanged(status: GnssStatus) {
            satelliteCount = 0
            cn0Values.clear()
            for (i in 0 until status.satelliteCount) {
                if (status.usedInFix(i)) {
                    satelliteCount++
                    cn0Values.add(status.getCn0DbHz(i))
                }
            }
        }
    }

    // ─── Helper computations ─────────────────────────────────────────────────

    private fun computeMotionVariance(): Double {
        if (accelBuffer.size < 10) return 0.0
        val mean = accelBuffer.average()
        return accelBuffer.map { (it - mean) * (it - mean) }.average()
    }

    private fun computeHeadingUncertainty(): Double {
        // Estimate heading uncertainty from magnetometer vs gyroscope agreement
        val magStrength = sqrt(mag[0] * mag[0] + mag[1] * mag[1] + mag[2] * mag[2])
        return when {
            magStrength < 25f || magStrength > 65f -> 30.0  // interference
            else -> 5.0 + (motionVariance * 2.0).coerceAtMost(25.0)
        }
    }

    private fun estimateStepLength(accMag: Double): Double {
        val baseLength = 0.65
        val k = 0.4
        val accVariation = (accMag - 9.81).coerceAtLeast(0.0)
        return baseLength + k * accVariation.pow(0.25)
    }

    // ─── Initialization ──────────────────────────────────────────────────────

    private fun initSensors() {
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
        magnetometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
        stepDetectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)
    }

    private fun startSensors() {
        val rate = SensorManager.SENSOR_DELAY_GAME  // ~50Hz

        accelSensor?.let { sensorManager.registerListener(this, it, rate) }
        gyroSensor?.let { sensorManager.registerListener(this, it, rate) }
        magnetometerSensor?.let { sensorManager.registerListener(this, it, rate) }
        stepDetectorSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL) }

        try {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L,   // min 1 second
                0.5f,    // min 0.5 meter
                this
            )
            locationManager.registerGnssStatusCallback(gnssStatusCallback, Handler(mainLooper))
        } catch (e: SecurityException) {
            Log.e(TAG, "Location permission not granted: ${e.message}")
        }
    }

    private fun stopSensors() {
        sensorManager.unregisterListener(this)
        try {
            locationManager.removeUpdates(this)
            locationManager.unregisterGnssStatusCallback(gnssStatusCallback)
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping location: ${e.message}")
        }
    }

    private fun resetAll() {
        gnssProcessor.reset()
        pdrProcessor.reset()
        kalmanProcessor.reset()
        fixedLagProcessor.reset()
        adaptiveFgoProcessor.reset()
        cloudSync.reset()
        TrajectoryRepository.reset()
    }

    // ─── Notification ────────────────────────────────────────────────────────

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "AdaptiveFGO Localization",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Background localization processing"
            setShowBadge(false)
        }
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification {
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("AdaptiveFGO Active")
            .setContentText("Computing trajectories...")
            .setSmallIcon(R.drawable.ic_navigation)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()
    }

    private var lastNotificationUpdate = 0L
    private fun updateNotification(snapshot: SensorSnapshot) {
        val now = System.currentTimeMillis()
        if (now - lastNotificationUpdate < 3000) return
        lastNotificationUpdate = now

        val gnss = snapshot.gnss
        val text = if (gnss.isValid) {
            "GPS: ${gnss.satelliteCount} sats, ±${gnss.accuracy.toInt()}m | Heading: ${heading.toInt()}°"
        } else {
            "Acquiring GPS signal... | Heading: ${heading.toInt()}°"
        }

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("AdaptiveFGO Active")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_navigation)
            .setOngoing(true)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .notify(NOTIFICATION_ID, notification)
    }

    // ─── Lifecycle ───────────────────────────────────────────────────────────

    override fun onDestroy() {
        processingJob?.cancel()
        serviceScope.cancel()
        stopSensors()
        cloudSync.destroy()
        TrajectoryRepository.setServiceRunning(false)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
