package com.adaptivefgo.navigator.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// ─── Sensor Snapshot (collected once, distributed to all algorithms) ───────────

data class GnssData(
    val timestamp: Long = 0L,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val accuracy: Float = 0f,
    val speed: Float = 0f,
    val satelliteCount: Int = 0,
    val cn0Values: List<Float> = emptyList(),  // Carrier-to-noise density (signal strength)
    val isValid: Boolean = false
) {
    val avgCn0: Float get() = if (cn0Values.isEmpty()) 0f else cn0Values.average().toFloat()
}

data class ImuData(
    val timestamp: Long = 0L,
    val ax: Float = 0f,  // Accelerometer X
    val ay: Float = 0f,  // Accelerometer Y
    val az: Float = 0f,  // Accelerometer Z
    val gx: Float = 0f,  // Gyroscope X
    val gy: Float = 0f,  // Gyroscope Y
    val gz: Float = 0f,  // Gyroscope Z
    val mx: Float = 0f,  // Magnetometer X
    val my: Float = 0f,  // Magnetometer Y
    val mz: Float = 0f,  // Magnetometer Z
)

data class SensorSnapshot(
    val timestamp: Long = System.currentTimeMillis(),
    val gnss: GnssData = GnssData(),
    val imu: ImuData = ImuData(),
    val heading: Double = 0.0,         // Degrees from North
    val stepDetected: Boolean = false,
    val stepLength: Double = 0.65,     // meters
    val motionVariance: Double = 0.0,  // PDR motion stability
    val headingUncertainty: Double = 10.0  // degrees
)

// ─── Trajectory Point ───────────────────────────────────────────────────────

@Parcelize
data class TrajectoryPoint(
    val timestamp: Long = 0L,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val accuracy: Float = Float.MAX_VALUE
) : Parcelable

// ─── Algorithm Types ────────────────────────────────────────────────────────

enum class AlgorithmType {
    GNSS,
    PDR,
    KALMAN_FILTER,
    BATCH_FGO,
    FIXED_LAG_FGO,
    ADAPTIVE_FGO
}

// ─── Trajectory Result ──────────────────────────────────────────────────────

data class TrajectoryResult(
    val algorithm: AlgorithmType,
    val points: List<TrajectoryPoint> = emptyList(),
    val rmse: Double = 0.0,
    val meanError: Double = 0.0,
    val maxError: Double = 0.0,
    val computationTimeMs: Long = 0L,
    val isComputing: Boolean = false
)

// ─── Error Metrics ──────────────────────────────────────────────────────────

data class ErrorMetrics(
    val algorithm: AlgorithmType,
    val rmse: Double,
    val meanError: Double,
    val maxError: Double,
    val errorOverTime: List<Pair<Long, Double>> = emptyList(),
    val windowSize: Int = 0  // For FGO algorithms
)

// ─── PDR State ──────────────────────────────────────────────────────────────

data class PdrState(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val heading: Double = 0.0,
    val stepCount: Int = 0,
    val cumulativeDistance: Double = 0.0
)

// ─── Kalman State ───────────────────────────────────────────────────────────

data class KalmanState(
    val x: DoubleArray = DoubleArray(4),   // [lat, lon, vLat, vLon]
    val P: Array<DoubleArray> = Array(4) { DoubleArray(4) }  // Covariance matrix
)

// ─── FGO Factor ─────────────────────────────────────────────────────────────

data class FgoFactor(
    val timestamp: Long,
    val gnssLat: Double,
    val gnssLon: Double,
    val gnssNoise: Double,
    val pdrDeltaLat: Double,
    val pdrDeltaLon: Double,
    val pdrNoise: Double
)

// ─── Adaptive Window State ───────────────────────────────────────────────────

data class AdaptiveWindowState(
    val currentWindowSize: Int = 10,
    val gnssQuality: Float = 1.0f,  // 0 = poor, 1 = good
    val pdrStability: Float = 1.0f, // 0 = unstable, 1 = stable
    val lastAdjustmentReason: String = ""
)

// ─── Navigation State ────────────────────────────────────────────────────────

data class NavigationState(
    val destinationLat: Double = 0.0,
    val destinationLon: Double = 0.0,
    val isNavigating: Boolean = false,
    val distanceRemaining: Float = 0f,
    val estimatedTimeMin: Int = 0
)

// ─── Algorithm Settings ──────────────────────────────────────────────────────

data class AlgorithmSettings(
    val stepLengthError: Float = 0.05f,     // meters
    val headingNoise: Float = 5.0f,          // degrees
    val gnssNoise: Float = 5.0f,             // meters
    val minWindowSize: Int = 5,
    val maxWindowSize: Int = 30,
    val adaptiveSensitivity: Float = 1.0f   // 0.5 = less responsive, 2.0 = more responsive
)
