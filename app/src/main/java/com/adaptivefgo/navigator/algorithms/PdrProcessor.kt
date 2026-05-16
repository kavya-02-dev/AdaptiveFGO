package com.adaptivefgo.navigator.algorithms

import com.adaptivefgo.navigator.data.*
import com.adaptivefgo.navigator.repository.TrajectoryRepository
import kotlin.math.*

/**
 * PDR Processor - Page 2
 * Pedestrian Dead Reckoning using step detection and heading estimation.
 * Uses: accelerometer for steps, magnetometer+gyroscope for heading.
 * Demonstrates cumulative drift without GNSS correction.
 */
class PdrProcessor {

    private val points = mutableListOf<TrajectoryPoint>()
    private var currentState = PdrState()
    private var isInitialized = false

    // Step detection state
    private var lastAccMagnitude = 0.0
    private var stepThreshold = 11.0  // m/s²
    private var stepCooldown = 0
    private val stepCooldownFrames = 10  // ~500ms at 20Hz

    // Heading smoothing
    private val headingBuffer = ArrayDeque<Double>(5)

    fun process(snapshot: SensorSnapshot) {
        // Initialize position from first valid GNSS fix
        if (!isInitialized) {
            if (snapshot.gnss.isValid) {
                currentState = PdrState(
                    latitude = snapshot.gnss.latitude,
                    longitude = snapshot.gnss.longitude,
                    heading = snapshot.heading
                )
                points.add(TrajectoryPoint(
                    timestamp = snapshot.timestamp,
                    latitude = currentState.latitude,
                    longitude = currentState.longitude
                ))
                isInitialized = true
            }
            return
        }

        // Update heading with smoothing
        headingBuffer.add(snapshot.heading)
        if (headingBuffer.size > 5) headingBuffer.removeFirst()
        val smoothedHeading = headingBuffer.average()
        currentState = currentState.copy(heading = smoothedHeading)

        // Step detection from accelerometer magnitude
        val imu = snapshot.imu
        val accMag = sqrt(imu.ax * imu.ax + imu.ay * imu.ay + imu.az * imu.az).toDouble()

        if (stepCooldown > 0) {
            stepCooldown--
        }

        val stepDetected = snapshot.stepDetected ||
                (accMag > stepThreshold && lastAccMagnitude <= stepThreshold && stepCooldown == 0)

        if (stepDetected && stepCooldown == 0) {
            stepCooldown = stepCooldownFrames

            // Estimate adaptive step length from acceleration magnitude
            val adaptiveStepLength = estimateStepLength(accMag)

            // Dead reckoning: move in heading direction by step length
            val headingRad = Math.toRadians(currentState.heading)
            val latPerMeter = 1.0 / 111320.0
            val lonPerMeter = 1.0 / (111320.0 * cos(Math.toRadians(currentState.latitude)))

            val newLat = currentState.latitude + adaptiveStepLength * cos(headingRad) * latPerMeter
            val newLon = currentState.longitude + adaptiveStepLength * sin(headingRad) * lonPerMeter

            currentState = currentState.copy(
                latitude = newLat,
                longitude = newLon,
                stepCount = currentState.stepCount + 1,
                cumulativeDistance = currentState.cumulativeDistance + adaptiveStepLength
            )

            points.add(TrajectoryPoint(
                timestamp = snapshot.timestamp,
                latitude = newLat,
                longitude = newLon
            ))

            TrajectoryRepository.updatePdr(
                TrajectoryResult(
                    algorithm = AlgorithmType.PDR,
                    points = points.toList()
                )
            )
        }

        lastAccMagnitude = accMag
    }

    /**
     * Weinberg step length estimation model.
     * Estimates step length from vertical acceleration magnitude.
     */
    private fun estimateStepLength(accMagnitude: Double): Double {
        // Weinberg model: L = K * (accMax - accMin)^(1/4)
        val settings = TrajectoryRepository.settings.value
        val baseLength = 0.65  // average step length in meters
        val k = 0.4
        val accVariation = (accMagnitude - 9.81).coerceAtLeast(0.0)
        return baseLength + k * accVariation.pow(0.25) * (1.0 + settings.stepLengthError)
    }

    fun getCurrentState() = currentState

    fun reset() {
        points.clear()
        currentState = PdrState()
        isInitialized = false
        lastAccMagnitude = 0.0
        stepCooldown = 0
        headingBuffer.clear()
    }
}
