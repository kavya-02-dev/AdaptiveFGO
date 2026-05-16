package com.adaptivefgo.navigator.algorithms

import com.adaptivefgo.navigator.data.*
import com.adaptivefgo.navigator.repository.TrajectoryRepository
import kotlin.math.cos
import kotlin.math.sqrt

/**
 * Kalman Filter Processor - Page 3
 * Classic GNSS + PDR fusion using an Extended Kalman Filter.
 * State vector: [latitude, longitude, velocity_lat, velocity_lon]
 * Demonstrates improved stability and reduced noise vs raw GNSS.
 */
class KalmanFilterProcessor {

    private val points = mutableListOf<TrajectoryPoint>()
    private var isInitialized = false
    private var lastTimestamp = 0L

    // State: [lat, lon, vel_lat, vel_lon]
    private var x = DoubleArray(4) { 0.0 }

    // State covariance matrix P (4x4)
    private var P = Array(4) { i -> DoubleArray(4) { j -> if (i == j) 1.0 else 0.0 } }

    // Process noise matrix Q
    private val Q_pos = 0.0001  // position process noise
    private val Q_vel = 0.001   // velocity process noise

    // Measurement noise
    private val R_gnss = 25.0   // GNSS noise variance (5m std dev)
    private val R_pdr = 0.5     // PDR noise variance

    fun process(snapshot: SensorSnapshot, pdrState: PdrState) {
        val gnss = snapshot.gnss
        val now = snapshot.timestamp

        if (!isInitialized) {
            if (!gnss.isValid) return

            x[0] = gnss.latitude
            x[1] = gnss.longitude
            x[2] = 0.0
            x[3] = 0.0

            P = Array(4) { i -> DoubleArray(4) { j ->
                when {
                    i == j && i < 2 -> gnss.accuracy.toDouble().let { it * it }
                    i == j -> 1.0
                    else -> 0.0
                }
            }}

            lastTimestamp = now
            isInitialized = true
            return
        }

        val dt = (now - lastTimestamp) / 1000.0
        lastTimestamp = now
        if (dt <= 0 || dt > 5.0) return

        // ── Prediction Step ─────────────────────────────────────────────────
        // State transition: position += velocity * dt
        val xPred = DoubleArray(4)
        xPred[0] = x[0] + x[2] * dt
        xPred[1] = x[1] + x[3] * dt
        xPred[2] = x[2]
        xPred[3] = x[3]

        // F = state transition matrix
        val F = Array(4) { i -> DoubleArray(4) { j ->
            when {
                i == j -> 1.0
                i == 0 && j == 2 -> dt
                i == 1 && j == 3 -> dt
                else -> 0.0
            }
        }}

        // P_pred = F * P * F^T + Q
        val FP = matMul(F, P)
        val FPFt = matMul(FP, transpose(F))
        val PPred = Array(4) { i -> DoubleArray(4) { j ->
            FPFt[i][j] + when {
                i == j && i < 2 -> Q_pos
                i == j -> Q_vel
                else -> 0.0
            }
        }}

        x = xPred
        P = PPred

        // ── Update Step: GNSS Measurement ────────────────────────────────────
        if (gnss.isValid) {
            val gnssNoiseFactor = (gnss.accuracy / 5.0).coerceIn(1.0, 10.0)
            val R = gnssNoiseFactor * R_gnss

            // Measurement matrix H (observe lat and lon only)
            // Innovation
            val innovation0 = gnss.latitude - x[0]
            val innovation1 = gnss.longitude - x[1]

            // Innovation covariance S = H*P*H^T + R (2x2)
            val S00 = P[0][0] + R
            val S11 = P[1][1] + R

            // Kalman gain K = P * H^T * S^-1
            val K0 = P[0][0] / S00
            val K1 = P[1][1] / S11

            // Update state
            x[0] += K0 * innovation0
            x[1] += K1 * innovation1
            x[2] += P[2][0] / S00 * innovation0 + P[2][1] / S11 * innovation1
            x[3] += P[3][0] / S00 * innovation0 + P[3][1] / S11 * innovation1

            // Update covariance (Joseph form for numerical stability)
            P[0][0] *= (1 - K0)
            P[1][1] *= (1 - K1)
        }

        // ── Update Step: PDR Measurement ─────────────────────────────────────
        if (snapshot.stepDetected && pdrState.stepCount > 0) {
            val innovation0 = pdrState.latitude - x[0]
            val innovation1 = pdrState.longitude - x[1]

            val settings = TrajectoryRepository.settings.value
            val pdrNoise = R_pdr * (1.0 + settings.stepLengthError * 10)

            val S00 = P[0][0] + pdrNoise
            val S11 = P[1][1] + pdrNoise

            val K0 = P[0][0] / S00 * 0.3  // Weight PDR less than GNSS
            val K1 = P[1][1] / S11 * 0.3

            x[0] += K0 * innovation0
            x[1] += K1 * innovation1

            P[0][0] *= (1 - K0)
            P[1][1] *= (1 - K1)
        }

        points.add(TrajectoryPoint(
            timestamp = now,
            latitude = x[0],
            longitude = x[1],
            accuracy = sqrt(P[0][0] + P[1][1]).toFloat()
        ))

        TrajectoryRepository.updateKalman(
            TrajectoryResult(
                algorithm = AlgorithmType.KALMAN_FILTER,
                points = points.toList()
            )
        )
    }

    private fun matMul(A: Array<DoubleArray>, B: Array<DoubleArray>): Array<DoubleArray> {
        val n = A.size
        val m = B[0].size
        val p = B.size
        return Array(n) { i ->
            DoubleArray(m) { j ->
                (0 until p).sumOf { k -> A[i][k] * B[k][j] }
            }
        }
    }

    private fun transpose(A: Array<DoubleArray>): Array<DoubleArray> {
        val n = A.size
        val m = A[0].size
        return Array(m) { i -> DoubleArray(n) { j -> A[j][i] } }
    }

    fun reset() {
        points.clear()
        isInitialized = false
        lastTimestamp = 0L
        x = DoubleArray(4)
        P = Array(4) { i -> DoubleArray(4) { j -> if (i == j) 1.0 else 0.0 } }
    }
}
