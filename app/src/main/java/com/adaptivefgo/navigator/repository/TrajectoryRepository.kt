package com.adaptivefgo.navigator.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.adaptivefgo.navigator.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Central repository that stores all computed trajectories.
 * All algorithms write here; all UI pages read from here.
 * This decouples algorithms from UI completely.
 */
object TrajectoryRepository {

    // ─── Trajectory Storage ─────────────────────────────────────────────────

    private val _gnssTrajectory = MutableStateFlow(TrajectoryResult(AlgorithmType.GNSS))
    val gnssTrajectory: StateFlow<TrajectoryResult> = _gnssTrajectory.asStateFlow()

    private val _pdrTrajectory = MutableStateFlow(TrajectoryResult(AlgorithmType.PDR))
    val pdrTrajectory: StateFlow<TrajectoryResult> = _pdrTrajectory.asStateFlow()

    private val _kalmanTrajectory = MutableStateFlow(TrajectoryResult(AlgorithmType.KALMAN_FILTER))
    val kalmanTrajectory: StateFlow<TrajectoryResult> = _kalmanTrajectory.asStateFlow()

    private val _batchFgoTrajectory = MutableStateFlow(TrajectoryResult(AlgorithmType.BATCH_FGO))
    val batchFgoTrajectory: StateFlow<TrajectoryResult> = _batchFgoTrajectory.asStateFlow()

    private val _fixedLagTrajectory = MutableStateFlow(TrajectoryResult(AlgorithmType.FIXED_LAG_FGO))
    val fixedLagTrajectory: StateFlow<TrajectoryResult> = _fixedLagTrajectory.asStateFlow()

    private val _adaptiveFgoTrajectory = MutableStateFlow(TrajectoryResult(AlgorithmType.ADAPTIVE_FGO))
    val adaptiveFgoTrajectory: StateFlow<TrajectoryResult> = _adaptiveFgoTrajectory.asStateFlow()

    // ─── Adaptive Window State ───────────────────────────────────────────────

    private val _adaptiveWindowState = MutableStateFlow(AdaptiveWindowState())
    val adaptiveWindowState: StateFlow<AdaptiveWindowState> = _adaptiveWindowState.asStateFlow()

    // ─── Latest Sensor Data ──────────────────────────────────────────────────

    private val _latestSnapshot = MutableStateFlow<SensorSnapshot?>(null)
    val latestSnapshot: StateFlow<SensorSnapshot?> = _latestSnapshot.asStateFlow()

    // ─── Navigation State ────────────────────────────────────────────────────

    private val _navigationState = MutableStateFlow(NavigationState())
    val navigationState: StateFlow<NavigationState> = _navigationState.asStateFlow()

    // ─── Settings ────────────────────────────────────────────────────────────

    private val _settings = MutableStateFlow(AlgorithmSettings())
    val settings: StateFlow<AlgorithmSettings> = _settings.asStateFlow()

    // ─── Service Running State ───────────────────────────────────────────────

    private val _isServiceRunning = MutableStateFlow(false)
    val isServiceRunning: StateFlow<Boolean> = _isServiceRunning.asStateFlow()

    // ─── Update Methods ──────────────────────────────────────────────────────

    fun updateGnss(result: TrajectoryResult) {
        _gnssTrajectory.value = result
    }

    fun updatePdr(result: TrajectoryResult) {
        _pdrTrajectory.value = result
    }

    fun updateKalman(result: TrajectoryResult) {
        _kalmanTrajectory.value = result
    }

    fun updateBatchFgo(result: TrajectoryResult) {
        _batchFgoTrajectory.value = result
    }

    fun updateFixedLag(result: TrajectoryResult) {
        _fixedLagTrajectory.value = result
    }

    fun updateAdaptiveFgo(result: TrajectoryResult) {
        _adaptiveFgoTrajectory.value = result
    }

    fun updateAdaptiveWindowState(state: AdaptiveWindowState) {
        _adaptiveWindowState.value = state
    }

    fun updateLatestSnapshot(snapshot: SensorSnapshot) {
        _latestSnapshot.value = snapshot
    }

    fun updateNavigationState(state: NavigationState) {
        _navigationState.value = state
    }

    fun updateSettings(settings: AlgorithmSettings) {
        _settings.value = settings
    }

    fun setServiceRunning(running: Boolean) {
        _isServiceRunning.value = running
    }

    // ─── Helper: Get all trajectories for comparison ─────────────────────────

    fun getAllTrajectories(): Map<AlgorithmType, TrajectoryResult> = mapOf(
        AlgorithmType.GNSS to _gnssTrajectory.value,
        AlgorithmType.PDR to _pdrTrajectory.value,
        AlgorithmType.KALMAN_FILTER to _kalmanTrajectory.value,
        AlgorithmType.BATCH_FGO to _batchFgoTrajectory.value,
        AlgorithmType.FIXED_LAG_FGO to _fixedLagTrajectory.value,
        AlgorithmType.ADAPTIVE_FGO to _adaptiveFgoTrajectory.value
    )

    // ─── Reset ───────────────────────────────────────────────────────────────

    fun reset() {
        _gnssTrajectory.value = TrajectoryResult(AlgorithmType.GNSS)
        _pdrTrajectory.value = TrajectoryResult(AlgorithmType.PDR)
        _kalmanTrajectory.value = TrajectoryResult(AlgorithmType.KALMAN_FILTER)
        _batchFgoTrajectory.value = TrajectoryResult(AlgorithmType.BATCH_FGO)
        _fixedLagTrajectory.value = TrajectoryResult(AlgorithmType.FIXED_LAG_FGO)
        _adaptiveFgoTrajectory.value = TrajectoryResult(AlgorithmType.ADAPTIVE_FGO)
        _adaptiveWindowState.value = AdaptiveWindowState()
        _latestSnapshot.value = null
    }

    // ─── Error Metrics ───────────────────────────────────────────────────────

    fun computeErrorMetrics(
        reference: List<TrajectoryPoint>,
        computed: List<TrajectoryPoint>
    ): Triple<Double, Double, Double> {
        if (reference.isEmpty() || computed.isEmpty()) return Triple(0.0, 0.0, 0.0)

        val errors = mutableListOf<Double>()
        val n = minOf(reference.size, computed.size)

        for (i in 0 until n) {
            val dist = haversineDistance(
                reference[i].latitude, reference[i].longitude,
                computed[i].latitude, computed[i].longitude
            )
            errors.add(dist)
        }

        val mean = errors.average()
        val rmse = Math.sqrt(errors.map { it * it }.average())
        val max = errors.maxOrNull() ?: 0.0

        return Triple(rmse, mean, max)
    }

    fun haversineDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371000.0  // Earth radius in meters
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2).let { it * it } +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2).let { it * it }
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c
    }
}
