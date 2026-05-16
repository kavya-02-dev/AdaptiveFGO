package com.adaptivefgo.navigator.ui.pdr

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

class PdrMapFragment : BaseMapFragment() {
    private var trajectory: Polyline? = null
    private lateinit var tvStats: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_map_single, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvStats = view.findViewById(R.id.tv_stats)
        view.findViewById<TextView>(R.id.tv_title).text = "PDR Inertial Path"
        initMapView(view, R.id.map_view, savedInstanceState)
    }

    override fun onMapReady(map: GoogleMap) {
        lifecycleScope.launch {
            TrajectoryRepository.pdrTrajectory.collectLatest { result ->
                val m = googleMap ?: return@collectLatest
                val points = result.points
                if (points.isEmpty()) return@collectLatest
                trajectory?.remove()
                trajectory = drawTrajectory(m, points, COLORS[AlgorithmType.PDR]!!, 4f)
                if (points.size == 1) addStartMarker(m, points.first())
                addEndMarker(m, points.last())
                centerOnTrajectory(m, points)

                val snap = TrajectoryRepository.latestSnapshot.value
                tvStats.text = buildString {
                    appendLine("PDR Inertial Trajectory  |  ${points.size} steps")
                    appendLine("Heading: ${"%.1f".format(snap?.heading ?: 0.0)}°  |  Step length: ${"%.2f".format(snap?.stepLength ?: 0.65)}m")
                    appendLine("Motion variance: ${"%.3f".format(snap?.motionVariance ?: 0.0)}")
                    append("⚠ Cumulative drift without GNSS correction visible over time")
                }
            }
        }
    }
}
