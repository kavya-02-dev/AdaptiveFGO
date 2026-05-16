package com.adaptivefgo.navigator.ui.kalman

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.adaptivefgo.navigator.R
import com.adaptivefgo.navigator.data.AlgorithmType
import com.adaptivefgo.navigator.repository.TrajectoryRepository
import com.adaptivefgo.navigator.ui.BaseMapFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Polyline
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class KalmanMapFragment : BaseMapFragment() {
    private var trajectory: Polyline? = null
    private lateinit var tvStats: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_map_single, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvStats = view.findViewById(R.id.tv_stats)
        view.findViewById<TextView>(R.id.tv_title).text = "Kalman Filter Fusion"
        initMapView(view, R.id.map_view, savedInstanceState)
    }

    override fun onMapReady(map: GoogleMap) {
        lifecycleScope.launch {
            TrajectoryRepository.kalmanTrajectory.collectLatest { result ->
                val m = googleMap ?: return@collectLatest
                val points = result.points
                if (points.isEmpty()) return@collectLatest
                trajectory?.remove()
                trajectory = drawTrajectory(m, points, COLORS[AlgorithmType.KALMAN_FILTER]!!, 4f)
                if (points.size == 1) addStartMarker(m, points.first())
                addEndMarker(m, points.last())
                centerOnTrajectory(m, points)
                tvStats.text = buildString {
                    appendLine("Kalman Filter GNSS+PDR Fusion  |  ${points.size} pts")
                    appendLine("Classical EKF with 4-state vector [lat, lon, vLat, vLon]")
                    append("✓ Improved stability and noise reduction vs raw GNSS")
                }
            }
        }
    }
}
