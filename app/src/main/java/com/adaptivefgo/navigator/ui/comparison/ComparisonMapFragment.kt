package com.adaptivefgo.navigator.ui.comparison

import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.adaptivefgo.navigator.R
import com.adaptivefgo.navigator.data.AlgorithmType
import com.adaptivefgo.navigator.data.TrajectoryPoint
import com.adaptivefgo.navigator.repository.TrajectoryRepository
import com.adaptivefgo.navigator.ui.BaseMapFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Polyline
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ComparisonMapFragment : BaseMapFragment() {

    private val polylines = mutableMapOf<AlgorithmType, Polyline>()
    private lateinit var legendContainer: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_comparison_map, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        legendContainer = view.findViewById(R.id.legend_container)
        view.findViewById<TextView>(R.id.tv_title).text = "All Algorithms Comparison"
        buildLegend()
        initMapView(view, R.id.map_view, savedInstanceState)
    }

    private fun buildLegend() {
        legendContainer.removeAllViews()
        AlgorithmType.values().forEach { algo ->
            val color = COLORS[algo] ?: return@forEach
            val name = NAMES[algo] ?: return@forEach
            val tv = TextView(requireContext()).apply {
                text = "━  $name"
                setTextColor(color)
                textSize = 11f
                setPadding(0, 2, 8, 2)
            }
            legendContainer.addView(tv)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        lifecycleScope.launch {
            combine(
                TrajectoryRepository.gnssTrajectory,
                TrajectoryRepository.pdrTrajectory,
                TrajectoryRepository.kalmanTrajectory,
                TrajectoryRepository.batchFgoTrajectory,
                TrajectoryRepository.fixedLagTrajectory,
                TrajectoryRepository.adaptiveFgoTrajectory
            ) { results -> results }
                .collect { results ->
                    val m = googleMap ?: return@collect

                    val allData = mapOf(
                        AlgorithmType.GNSS to results[0].points,
                        AlgorithmType.PDR to results[1].points,
                        AlgorithmType.KALMAN_FILTER to results[2].points,
                        AlgorithmType.BATCH_FGO to results[3].points,
                        AlgorithmType.FIXED_LAG_FGO to results[4].points,
                        AlgorithmType.ADAPTIVE_FGO to results[5].points
                    )

                    // Clear old polylines
                    polylines.values.forEach { it.remove() }
                    polylines.clear()

                    val allPoints = mutableListOf<TrajectoryPoint>()

                    allData.forEach { (algo, points) ->
                        if (points.size >= 2) {
                            val width = if (algo == AlgorithmType.ADAPTIVE_FGO) 7f else 3f
                            val polyline = drawTrajectory(m, points, COLORS[algo]!!, width)
                            polyline?.let { polylines[algo] = it }
                            allPoints.addAll(points)
                        }
                    }

                    if (allPoints.isNotEmpty()) {
                        fitBoundsToTrajectory(m, allPoints)
                    }
                }
        }
    }
}
