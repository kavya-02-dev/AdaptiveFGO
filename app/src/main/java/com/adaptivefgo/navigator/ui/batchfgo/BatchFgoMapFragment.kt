package com.adaptivefgo.navigator.ui.batchfgo

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

class BatchFgoMapFragment : BaseMapFragment() {
    private var trajectory: Polyline? = null
    private lateinit var tvStats: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_map_batch, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvStats = view.findViewById(R.id.tv_stats)
        progressBar = view.findViewById(R.id.progress_cloud)
        view.findViewById<TextView>(R.id.tv_title).text = "Batch FGO (GCP Cloud)"
        initMapView(view, R.id.map_view, savedInstanceState)
    }

    override fun onMapReady(map: GoogleMap) {
        lifecycleScope.launch {
            TrajectoryRepository.batchFgoTrajectory.collectLatest { result ->
                val m = googleMap ?: return@collectLatest
                progressBar.visibility = if (result.isComputing) android.view.View.VISIBLE else android.view.View.GONE

                val points = result.points
                if (points.isEmpty()) {
                    tvStats.text = "Batch FGO (GCP Cloud Run)\nAccumulating data for cloud optimization...\n(Syncs every 30 seconds)"
                    return@collectLatest
                }

                trajectory?.remove()
                trajectory = drawTrajectory(m, points, COLORS[AlgorithmType.BATCH_FGO]!!, 4f)
                if (points.size >= 1) addStartMarker(m, points.first())
                addEndMarker(m, points.last())
                fitBoundsToTrajectory(m, points)

                tvStats.text = buildString {
                    appendLine("Batch FGO (GCP Cloud Run)  |  ${points.size} pts")
                    if (result.computationTimeMs > 0)
                        appendLine("Cloud compute time: ${result.computationTimeMs}ms")
                    append("Full graph optimization — highest accuracy, offline capable")
                }
            }
        }
    }
}
