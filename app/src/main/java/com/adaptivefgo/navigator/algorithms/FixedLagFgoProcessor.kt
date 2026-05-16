package com.adaptivefgo.navigator.algorithms

import com.adaptivefgo.navigator.data.*
import com.adaptivefgo.navigator.repository.TrajectoryRepository
import kotlin.math.*

/**
 * Fixed-Lag FGO Processor - Page 5
 * Sliding window Factor Graph Optimization.
 * Uses a fixed-size window of past measurements to optimize trajectory.
 * Demonstrates near-optimal accuracy with bounded computational cost.
 */
class FixedLagFgoProcessor(private val windowSize: Int = 10) {

    private val allPoints = mutableListOf<TrajectoryPoint>()
    private val factorWindow = ArrayDeque<FgoFactor>()

    // Current optimized state
    private var currentLat = 0.0
    private var currentLon = 0.0
    private var isInitialized = false

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

        // Add new factor to window
        if (gnss.isValid || snapshot.stepDetected) {
            val settings = TrajectoryRepository.settings.value
            val gnssNoise = if (gnss.isValid) {
                (gnss.accuracy * (1.0 + settings.gnssNoise / 10.0)).toDouble()
            } else 50.0  // Large noise when GNSS invalid

            // PDR delta from current position
            val pdrDLat = if (snapshot.stepDetected) pdrState.latitude - currentLat else 0.0
            val pdrDLon = if (snapshot.stepDetected) pdrState.longitude - currentLon else 0.0

            val factor = FgoFactor(
                timestamp = snapshot.timestamp,
                gnssLat = if (gnss.isValid) gnss.latitude else currentLat,
                gnssLon = if (gnss.isValid) gnss.longitude else currentLon,
                gnssNoise = gnssNoise,
                pdrDeltaLat = pdrDLat,
                pdrDeltaLon = pdrDLon,
                pdrNoise = settings.stepLengthError.toDouble() * 2.0 + 0.5
            )

            factorWindow.add(factor)

            // Maintain fixed window size
            while (factorWindow.size > windowSize) {
                factorWindow.removeFirst()
            }

            // Run FGO optimization on current window
            val optimized = optimizeWindow(factorWindow.toList())
            currentLat = optimized.first
            currentLon = optimized.second

            allPoints.add(TrajectoryPoint(
                timestamp = snapshot.timestamp,
                latitude = currentLat,
                longitude = currentLon
            ))

            TrajectoryRepository.updateFixedLag(
                TrajectoryResult(
                    algorithm = AlgorithmType.FIXED_LAG_FGO,
                    points = allPoints.toList()
                )
            )
        }
    }

    /**
     * Gauss-Newton optimization of factor graph within window.
     * Minimizes sum of squared weighted residuals from GNSS and PDR factors.
     */
    private fun optimizeWindow(factors: List<FgoFactor>): Pair<Double, Double> {
        if (factors.isEmpty()) return Pair(currentLat, currentLon)

        // Initial estimate: weighted average of GNSS measurements
        var estLat = factors.filter { it.gnssNoise < 20.0 }
            .map { it.gnssLat }.average()
            .let { if (it.isNaN()) currentLat else it }
        var estLon = factors.filter { it.gnssNoise < 20.0 }
            .map { it.gnssLon }.average()
            .let { if (it.isNaN()) currentLon else it }

        // Gauss-Newton iterations
        repeat(5) {
            var H_lat = 0.0
            var H_lon = 0.0
            var b_lat = 0.0
            var b_lon = 0.0

            for (factor in factors) {
                // GNSS factor: residual = measurement - estimate
                val wGnss = 1.0 / (factor.gnssNoise * factor.gnssNoise)
                b_lat += wGnss * (factor.gnssLat - estLat)
                b_lon += wGnss * (factor.gnssLon - estLon)
                H_lat += wGnss
                H_lon += wGnss

                // PDR factor: adds motion constraint
                if (abs(factor.pdrDeltaLat) > 1e-10 || abs(factor.pdrDeltaLon) > 1e-10) {
                    val wPdr = 1.0 / (factor.pdrNoise * factor.pdrNoise)
                    b_lat += wPdr * factor.pdrDeltaLat * 0.5
                    b_lon += wPdr * factor.pdrDeltaLon * 0.5
                    H_lat += wPdr * 0.25
                    H_lon += wPdr * 0.25
                }
            }

            if (H_lat > 0 && H_lon > 0) {
                val delta_lat = b_lat / H_lat
                val delta_lon = b_lon / H_lon
                estLat += delta_lat.coerceIn(-0.001, 0.001)
                estLon += delta_lon.coerceIn(-0.001, 0.001)
            }
        }

        return Pair(estLat, estLon)
    }

    fun reset() {
        allPoints.clear()
        factorWindow.clear()
        isInitialized = false
    }
}
