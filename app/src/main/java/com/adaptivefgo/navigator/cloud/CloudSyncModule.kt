package com.adaptivefgo.navigator.cloud

import android.util.Log
import com.adaptivefgo.navigator.data.*
import com.adaptivefgo.navigator.repository.TrajectoryRepository
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

/**
 * Cloud Sync Module - Page 4 (Batch FGO)
 * Sends accumulated trajectory data to GCP Cloud Run Python service.
 * Receives back globally-optimized trajectory.
 *
 * Configure GCP_BATCH_FGO_URL in local.properties:
 *   GCP_BATCH_FGO_URL=https://your-service-xxxxxxxx-uc.a.run.app/optimize
 */
class CloudSyncModule {

    companion object {
        private const val TAG = "CloudSyncModule"
        private const val SYNC_INTERVAL_MS = 30_000L  // sync every 30 seconds
        private const val MIN_FACTORS_TO_SYNC = 10
    }

    private val gson = Gson()
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val pendingFactors = mutableListOf<FgoFactor>()
    private var lastSyncTime = 0L
    private var isSyncing = false
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    fun addFactor(factor: FgoFactor) {
        synchronized(pendingFactors) {
            pendingFactors.add(factor)
        }
    }

    fun maybeSync(gcpUrl: String) {
        val now = System.currentTimeMillis()
        if (isSyncing) return
        if (now - lastSyncTime < SYNC_INTERVAL_MS) return
        if (pendingFactors.size < MIN_FACTORS_TO_SYNC) return
        if (gcpUrl.isBlank()) return

        scope.launch { syncToCloud(gcpUrl) }
    }

    private suspend fun syncToCloud(gcpUrl: String) {
        isSyncing = true
        TrajectoryRepository.updateBatchFgo(
            TrajectoryRepository.batchFgoTrajectory.value.copy(isComputing = true)
        )

        val startTime = System.currentTimeMillis()
        val factorsToSend: List<FgoFactor>
        synchronized(pendingFactors) {
            factorsToSend = pendingFactors.toList()
        }

        try {
            val request = BatchFgoRequest(
                factors = factorsToSend.map { factor ->
                    FactorDto(
                        timestamp = factor.timestamp,
                        gnssLat = factor.gnssLat,
                        gnssLon = factor.gnssLon,
                        gnssNoise = factor.gnssNoise,
                        pdrDeltaLat = factor.pdrDeltaLat,
                        pdrDeltaLon = factor.pdrDeltaLon,
                        pdrNoise = factor.pdrNoise
                    )
                }
            )

            val json = gson.toJson(request)
            val body = json.toRequestBody("application/json".toMediaType())
            val httpRequest = Request.Builder()
                .url(gcpUrl)
                .post(body)
                .header("Content-Type", "application/json")
                .build()

            val response = withContext(Dispatchers.IO) {
                client.newCall(httpRequest).execute()
            }

            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                if (responseBody != null) {
                    val result = gson.fromJson(responseBody, BatchFgoResponse::class.java)
                    val computeMs = System.currentTimeMillis() - startTime

                    val trajectoryPoints = result.optimizedTrajectory.map { pt ->
                        TrajectoryPoint(
                            timestamp = pt.timestamp,
                            latitude = pt.latitude,
                            longitude = pt.longitude,
                            accuracy = pt.accuracy
                        )
                    }

                    TrajectoryRepository.updateBatchFgo(
                        TrajectoryResult(
                            algorithm = AlgorithmType.BATCH_FGO,
                            points = trajectoryPoints,
                            computationTimeMs = computeMs,
                            isComputing = false
                        )
                    )

                    Log.i(TAG, "Batch FGO success: ${trajectoryPoints.size} points, ${computeMs}ms")
                    lastSyncTime = System.currentTimeMillis()
                }
            } else {
                Log.e(TAG, "Batch FGO HTTP error: ${response.code}")
                useFallbackBatchFgo(factorsToSend)
            }

        } catch (e: Exception) {
            Log.e(TAG, "Batch FGO sync failed: ${e.message}")
            useFallbackBatchFgo(factorsToSend)
        } finally {
            isSyncing = false
            TrajectoryRepository.updateBatchFgo(
                TrajectoryRepository.batchFgoTrajectory.value.copy(isComputing = false)
            )
        }
    }

    /**
     * Local fallback: performs simplified batch optimization on-device when
     * cloud is unavailable. Not as accurate as server-side Python GTSAM but
     * ensures Page 4 always shows data.
     */
    private fun useFallbackBatchFgo(factors: List<FgoFactor>) {
        val points = mutableListOf<TrajectoryPoint>()
        if (factors.isEmpty()) return

        var lat = factors.first().gnssLat
        var lon = factors.first().gnssLon

        for (factor in factors) {
            // Simple weighted average of GNSS and accumulated PDR
            val gnssW = 1.0 / (factor.gnssNoise * factor.gnssNoise)
            val currentW = 0.5
            val totalW = gnssW + currentW

            val newLat = (gnssW * factor.gnssLat + currentW * (lat + factor.pdrDeltaLat)) / totalW
            val newLon = (gnssW * factor.gnssLon + currentW * (lon + factor.pdrDeltaLon)) / totalW

            lat = newLat
            lon = newLon

            points.add(TrajectoryPoint(factor.timestamp, lat, lon))
        }

        TrajectoryRepository.updateBatchFgo(
            TrajectoryResult(
                algorithm = AlgorithmType.BATCH_FGO,
                points = points,
                isComputing = false
            )
        )
    }

    fun reset() {
        synchronized(pendingFactors) { pendingFactors.clear() }
        lastSyncTime = 0L
    }

    fun destroy() {
        scope.cancel()
    }
}

// ─── DTOs ────────────────────────────────────────────────────────────────────

data class BatchFgoRequest(
    @SerializedName("factors") val factors: List<FactorDto>
)

data class FactorDto(
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("gnss_lat") val gnssLat: Double,
    @SerializedName("gnss_lon") val gnssLon: Double,
    @SerializedName("gnss_noise") val gnssNoise: Double,
    @SerializedName("pdr_delta_lat") val pdrDeltaLat: Double,
    @SerializedName("pdr_delta_lon") val pdrDeltaLon: Double,
    @SerializedName("pdr_noise") val pdrNoise: Double
)

data class BatchFgoResponse(
    @SerializedName("optimized_trajectory") val optimizedTrajectory: List<TrajectoryPointDto>,
    @SerializedName("computation_time_ms") val computationTimeMs: Long = 0L
)

data class TrajectoryPointDto(
    @SerializedName("timestamp") val timestamp: Long,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("accuracy") val accuracy: Float = 1f
)
