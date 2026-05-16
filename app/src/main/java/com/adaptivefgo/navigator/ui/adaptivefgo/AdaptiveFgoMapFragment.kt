package com.adaptivefgo.navigator.ui.adaptivefgo

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.ProgressBar
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

class AdaptiveFgoMapFragment : BaseMapFragment() {
    private var trajectory: Polyline? = null
    private lateinit var tvStats: TextView
    private lateinit var tvAdaptive: TextView
    private lateinit var pbGnssQuality: ProgressBar
    private lateinit var pbPdrStability: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_adaptive_fgo, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvStats = view.findViewById(R.id.tv_stats)
        tvAdaptive = view.findViewById(R.id.tv_adaptive_info)
        pbGnssQuality = view.findViewById(R.id.pb_gnss_quality)
        pbPdrStability = view.findViewById(R.id.pb_pdr_stability)
        view.findViewById<TextView>(R.id.tv_title).text = "Adaptive FGO ⭐ (Research)"
        initMapView(view, R.id.map_view, savedInstanceState)
    }

    override fun onMapReady(map: GoogleMap) {
        observeTrajectory()
        observeAdaptiveState()
    }

    private fun observeTrajectory() {
        lifecycleScope.launch {
            TrajectoryRepository.adaptiveFgoTrajectory.collectLatest { result ->
                val m = googleMap ?: return@collectLatest
                val points = result.points
                if (points.isEmpty()) return@collectLatest
                trajectory?.remove()
                trajectory = drawTrajectory(m, points, COLORS[AlgorithmType.ADAPTIVE_FGO]!!, 5f)
                if (points.size == 1) addStartMarker(m, points.first())
                addEndMarker(m, points.last())
                centerOnTrajectory(m, points)
                tvStats.text = "Adaptive FGO  |  ${points.size} points optimized"
            }
        }
    }

    private fun observeAdaptiveState() {
        lifecycleScope.launch {
            TrajectoryRepository.adaptiveWindowState.collectLatest { state ->
                val windowColor = when {
                    state.currentWindowSize <= 8 -> "#4CAF50"   // Green = small window, good signal
                    state.currentWindowSize <= 18 -> "#FF9800"  // Orange = medium
                    else -> "#F44336"                            // Red = large, weak signal
                }

                tvAdaptive.text = buildString {
                    appendLine("🔬 ADAPTIVE WINDOW: ${state.currentWindowSize}")
                    appendLine(state.lastAdjustmentReason)
                }

                pbGnssQuality.progress = (state.gnssQuality * 100).toInt()
                pbPdrStability.progress = (state.pdrStability * 100).toInt()
            }
        }
    }
}
