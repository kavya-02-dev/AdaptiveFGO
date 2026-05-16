package com.adaptivefgo.navigator.algorithms

import com.adaptivefgo.navigator.data.*
import com.adaptivefgo.navigator.repository.TrajectoryRepository

/**
 * GNSS Processor - Page 1
 * Converts raw GNSS readings into a trajectory.
 * Demonstrates satellite noise, multipath error, signal instability.
 */
class GnssProcessor {

    private val points = mutableListOf<TrajectoryPoint>()

    fun process(snapshot: SensorSnapshot) {
        val gnss = snapshot.gnss
        if (!gnss.isValid) return

        val point = TrajectoryPoint(
            timestamp = gnss.timestamp,
            latitude = gnss.latitude,
            longitude = gnss.longitude,
            accuracy = gnss.accuracy
        )

        points.add(point)

        TrajectoryRepository.updateGnss(
            TrajectoryResult(
                algorithm = AlgorithmType.GNSS,
                points = points.toList(),
                computationTimeMs = 0L
            )
        )
    }

    fun reset() {
        points.clear()
    }
}
