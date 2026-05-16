package com.adaptivefgo.navigator.algorithms

import com.adaptivefgo.navigator.data.*
import com.adaptivefgo.navigator.repository.TrajectoryRepository
import kotlin.math.*

/**
 * Adaptive FGO Processor - Page 6 (Primary Research Contribution)
 *
 * Key Innovation: Window size dynamically adjusts based on:
 *   - GNSS signal quality (CN0, accuracy, satellite count)
 *   - PDR motion variance (step regularity)
 *   - Heading uncertainty
 *
 * Logic:
 *   Strong GNSS + stable motion  → smaller window (trust GNSS, less computation)
 *   Weak GNSS + unstable motion  → larger window (need more context to correct drift)
 *
 * This achieves better accuracy AND efficiency than fixed-window FGO.
 */
class AdaptiveFgoProcessor {

    private val allPoints = mutableListOf<TrajectoryPoint>()
    private val factorBuffer = mutableListOf<FgoFactor>()

    private var currentLat = 0.0
    private var currentLon = 0.0
    private var isInitialized = false

    // Adaptive window tracking
    private var currentWindowSize = 10
    private val minWindow = 5
    private val maxWindow = 30

    // Signal quality history for smooth adaptation
    private val cn0History = ArrayDeque<Float>(20)
    private val accVarianceHistory = ArrayDeque<Double>(20)

    fun process(snapshot: SensorSnapshot, pdrState: PdrState) {
        val gnss = snapshot.gnss

        if (!isInitialized) {
            if (!gnss.isValid) return
            currentLat = gnss.latitude
            currentLon = gnss.longitude
            allPoints.add(TrajectoryPoint(snapshot.timestamp, currentLat, currentLon))
            isInitialized = true
            return
        }

        // ── Update signal quality history ─────────────────────────────────────
        if (gnss.avgCn0 > 0) {
            cn0History.add(gnss.avgCn0)
            if (cn0History.size > 20) cn0History.removeFirst()
        }

        accVarianceHistory.add(snapshot.motionVariance)
        if (accVarianceHistory.size > 20) accVarianceHistory.removeFirst()

        // ── Compute Adaptive Window Size ──────────────────────────────────────
        val windowState = computeAdaptiveWindow(snapshot)
        currentWindowSize = windowState.currentWindowSize

        TrajectoryRepository.updateAdaptiveWindowState(windowState)

        // ── Add factor to buffer ──────────────────────────────────────────────
        if (gnss.isValid || snapshot.stepDetected) {
            val settings = TrajectoryRepository.settings.value
            val gnssNoise = if (gnss.isValid) {
                (gnss.accuracy * (1.0 + settings.gnssNoise / 10.0)).toDouble()
            } else 50.0

            val pdrDLat = if (snapshot.stepDetected) pdrState.latitude - currentLat else 0.0
            val pdrDLon = if (snapshot.stepDetected) pdrState.longitude - currentLon else 0.0

            factorBuffer.add(
                FgoFactor(
                    timestamp = snapshot.timestamp,
                    gnssLat = if (gnss.isValid) gnss.latitude else currentLat,
                    gnssLon = if (gnss.isValid) gnss.longitude else currentLon,
                    gnssNoise = gnssNoise,
                    pdrDeltaLat = pdrDLat,
                    pdrDeltaLon = pdrDLon,
                    pdrNoise = settings.stepLengthError.toDouble() * 2.0 + 0.3
                )
            )

            // Trim buffer to adaptive window size
            while (factorBuffer.size > currentWindowSize) {
                factorBuffer.removeAt(0)
            }

            // Optimize with current adaptive window
            val optimized = adaptiveOptimize(factorBuffer.toList(), windowState)
            currentLat = optimized.first
            currentLon = optimized.second

            allPoints.add(TrajectoryPoint(
                timestamp = snapshot.timestamp,
                latitude = currentLat,
                longitude = currentLon
            ))

            TrajectoryRepository.updateAdaptiveFgo(
                TrajectoryResult(
                    algorithm = AlgorithmType.ADAPTIVE_FGO,
                    points = allPoints.toList()
                )
            )
        }
    }

    /**
     * Core adaptive logic: computes window size and quality scores from sensor data.
     */
    private fun computeAdaptiveWindow(snapshot: SensorSnapshot): AdaptiveWindowState {
        val gnss = snapshot.gnss
        val settings = TrajectoryRepository.settings.value

        // ── GNSS Quality Score (0 = poor, 1 = excellent) ─────────────────────
        val cn0Score = if (cn0History.isEmpty()) 0.5f else {
            val avgCn0 = cn0History.average().toFloat()
            // CN0 range: 20 dB-Hz (poor) to 50 dB-Hz (excellent)
            ((avgCn0 - 20f) / 30f).coerceIn(0f, 1f)
        }

        val accuracyScore = if (gnss.isValid) {
            // Accuracy range: 50m (poor) to 1m (excellent)
            (1f - (gnss.accuracy - 1f) / 49f).coerceIn(0f, 1f)
        } else 0f

        val satScore = (gnss.satelliteCount / 12f).coerceIn(0f, 1f)

        val gnssQuality = (cn0Score * 0.4f + accuracyScore * 0.4f + satScore * 0.2f)

        // ── PDR Stability Score (0 = unstable, 1 = stable) ────────────────────
        val motionVar = if (accVarianceHistory.isEmpty()) 1.0
        else accVarianceHistory.average()

        // Variance range: 0.5 (stable walking) to 5.0 (erratic motion)
        val pdrStability = (1.0 - (motionVar - 0.5) / 4.5).coerceIn(0.0, 1.0).toFloat()

        val headingStability = 1f - (snapshot.headingUncertainty / 45.0).coerceIn(0.0, 1.0).toFloat()

        // ── Adaptive Window Calculation ────────────────────────────────────────
        // Combined quality: high quality = small window, low quality = large window
        val combinedQuality = gnssQuality * 0.6f + pdrStability * 0.3f + headingStability * 0.1f
        val sensitivity = settings.adaptiveSensitivity

        // Invert: poor signal → larger window to gather more context
        val windowRange = (settings.maxWindowSize - settings.minWindowSize).toFloat()
        val targetWindow = settings.minWindowSize + ((1f - combinedQuality) * windowRange * sensitivity).toInt()
        val adaptiveWindow = targetWindow.coerceIn(settings.minWindowSize, settings.maxWindowSize)

        // Smooth window transitions (avoid rapid changes)
        val smoothedWindow = ((currentWindowSize * 0.7 + adaptiveWindow * 0.3).toInt())
            .coerceIn(settings.minWindowSize, settings.maxWindowSize)

        val reason = buildAdaptationReason(gnssQuality, pdrStability, smoothedWindow)

        return AdaptiveWindowState(
            currentWindowSize = smoothedWindow,
            gnssQuality = gnssQuality,
            pdrStability = pdrStability,
            lastAdjustmentReason = reason
        )
    }

    private fun buildAdaptationReason(gnssQuality: Float, pdrStability: Float, windowSize: Int): String {
        return when {
            gnssQuality > 0.8f && pdrStability > 0.8f -> "Strong GNSS + stable motion → window=$windowSize (efficient)"
            gnssQuality < 0.3f && pdrStability < 0.3f -> "Weak GNSS + unstable motion → window=$windowSize (robust)"
            gnssQuality < 0.4f -> "Weak GNSS signal → window=$windowSize (correcting drift)"
            pdrStability < 0.4f -> "Unstable motion → window=$windowSize (smoothing)"
            else -> "Balanced conditions → window=$windowSize"
        }
    }

    /**
     * Adaptive Gauss-Newton optimization.
     * Applies dynamic information matrix weighting based on signal quality.
     */
    private fun adaptiveOptimize(
        factors: List<FgoFactor>,
        windowState: AdaptiveWindowState
    ): Pair<Double, Double> {
        if (factors.isEmpty()) return Pair(currentLat, currentLon)

        val gnssWeight = windowState.gnssQuality.toDouble().coerceIn(0.1, 1.0)
        val pdrWeight = windowState.pdrStability.toDouble().coerceIn(0.1, 1.0)

        var estLat = factors.last().gnssLat
        var estLon = factors.last().gnssLon

        // Gauss-Newton with adaptive information matrix
        repeat(8) {
            var H_lat = 0.0
            var H_lon = 0.0
            var b_lat = 0.0
            var b_lon = 0.0

            for ((idx, factor) in factors.withIndex()) {
                // Time-decaying weight: recent factors get more weight
                val timeWeight = 0.5 + 0.5 * (idx.toDouble() / factors.size)

                // GNSS factor with adaptive weighting
                val wGnss = gnssWeight * timeWeight / (factor.gnssNoise * factor.gnssNoise)
                b_lat += wGnss * (factor.gnssLat - estLat)
                b_lon += wGnss * (factor.gnssLon - estLon)
                H_lat += wGnss
                H_lon += wGnss

                // PDR factor with adaptive weighting
                if (abs(factor.pdrDeltaLat) > 1e-10 || abs(factor.pdrDeltaLon) > 1e-10) {
                    val wPdr = pdrWeight * timeWeight / (factor.pdrNoise * factor.pdrNoise)
                    b_lat += wPdr * factor.pdrDeltaLat * 0.4
                    b_lon += wPdr * factor.pdrDeltaLon * 0.4
                    H_lat += wPdr * 0.16
                    H_lon += wPdr * 0.16
                }
            }

            if (H_lat > 1e-10 && H_lon > 1e-10) {
                val delta_lat = (b_lat / H_lat).coerceIn(-0.0005, 0.0005)
                val delta_lon = (b_lon / H_lon).coerceIn(-0.0005, 0.0005)
                estLat += delta_lat
                estLon += delta_lon
            }
        }

        return Pair(estLat, estLon)
    }

    fun getCurrentWindowSize() = currentWindowSize

    fun reset() {
        allPoints.clear()
        factorBuffer.clear()
        isInitialized = false
        currentWindowSize = 10
        cn0History.clear()
        accVarianceHistory.clear()
    }
}
