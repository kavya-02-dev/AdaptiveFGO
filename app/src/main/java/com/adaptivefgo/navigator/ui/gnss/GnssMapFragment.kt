package com.adaptivefgo.navigator.ui.gnss

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

class GnssMapFragment : BaseMapFragment() {

    private var trajectory: Polyline? = null
    private lateinit var tvStats: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_map_single, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvStats = view.findViewById(R.id.tv_stats)
        tvStats.text = "GNSS Raw Trajectory\nBaseline: Satellite noise & multipath error visible"
        view.findViewById<TextView>(R.id.tv_title).text = "GNSS Raw Path"
        initMapView(view, R.id.map_view, savedInstanceState)
    }

    override fun onMapReady(map: GoogleMap) {
        observeTrajectory()
    }

    private fun observeTrajectory() {
        lifecycleScope.launch {
            TrajectoryRepository.gnssTrajectory.collectLatest { result ->
                val map = googleMap ?: return@collectLatest
                val points = result.points
                if (points.isEmpty()) return@collectLatest

                trajectory?.remove()
                trajectory = drawTrajectory(map, points, COLORS[AlgorithmType.GNSS]!!, 4f)

                if (points.size == 1) addStartMarker(map, points.first())
                addEndMarker(map, points.last())
                centerOnTrajectory(map, points)

                val latest = TrajectoryRepository.latestSnapshot.value
                val gnss = latest?.gnss
                tvStats.text = buildString {
                    appendLine("GNSS Raw Trajectory  |  ${points.size} points")
                    if (gnss?.isValid == true) {
                        appendLine("Accuracy: ±${gnss.accuracy.toInt()}m  |  Satellites: ${gnss.satelliteCount}")
                        appendLine("CN0 avg: ${"%.1f".format(gnss.avgCn0)} dB-Hz  |  Speed: ${"%.1f".format(gnss.speed * 3.6)} km/h")
                    } else {
                        appendLine("Acquiring GPS signal...")
                    }
                    append("⚠ Raw GNSS shows noise, multipath, and signal instability")
                }
            }
        }
    }
}
