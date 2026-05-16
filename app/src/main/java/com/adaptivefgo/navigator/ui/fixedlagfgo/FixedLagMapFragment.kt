package com.adaptivefgo.navigator.ui.fixedlagfgo

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

class FixedLagMapFragment : BaseMapFragment() {
    private var trajectory: Polyline? = null
    private lateinit var tvStats: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_map_single, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvStats = view.findViewById(R.id.tv_stats)
        view.findViewById<TextView>(R.id.tv_title).text = "Fixed-Lag FGO (Window=10)"
        initMapView(view, R.id.map_view, savedInstanceState)
    }

    override fun onMapReady(map: GoogleMap) {
        lifecycleScope.launch {
            TrajectoryRepository.fixedLagTrajectory.collectLatest { result ->
                val m = googleMap ?: return@collectLatest
                val points = result.points
                if (points.isEmpty()) return@collectLatest
                trajectory?.remove()
                trajectory = drawTrajectory(m, points, COLORS[AlgorithmType.FIXED_LAG_FGO]!!, 4f)
                if (points.size == 1) addStartMarker(m, points.first())
                addEndMarker(m, points.last())
                centerOnTrajectory(m, points)
                tvStats.text = buildString {
                    appendLine("Fixed-Lag FGO  |  Window=10  |  ${points.size} pts")
                    appendLine("Sliding window Gauss-Newton optimization")
                    append("✓ Near-optimal accuracy with bounded computation")
                }
            }
        }
    }
}
