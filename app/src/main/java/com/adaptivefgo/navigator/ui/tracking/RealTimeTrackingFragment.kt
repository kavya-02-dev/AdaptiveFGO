package com.adaptivefgo.navigator.ui.tracking

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.adaptivefgo.navigator.R
import com.adaptivefgo.navigator.data.AlgorithmType
import com.adaptivefgo.navigator.repository.TrajectoryRepository
import com.adaptivefgo.navigator.ui.BaseMapFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RealTimeTrackingFragment : BaseMapFragment() {

    private var gnssMarker: Marker? = null
    private var pdrMarker: Marker? = null
    private var adaptiveMarker: Marker? = null
    private lateinit var tvStats: TextView
    private var isFirstFix = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_map_single, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvStats = view.findViewById(R.id.tv_stats)
        view.findViewById<TextView>(R.id.tv_title).text = "Real-Time Live Tracking"
        initMapView(view, R.id.map_view, savedInstanceState)
    }

    override fun onMapReady(map: GoogleMap) {
        lifecycleScope.launch {
            TrajectoryRepository.latestSnapshot.collectLatest { snapshot ->
                snapshot ?: return@collectLatest
                val m = googleMap ?: return@collectLatest
                val gnss = snapshot.gnss

                // Update GNSS marker (blue)
                if (gnss.isValid) {
                    val gnssPos = LatLng(gnss.latitude, gnss.longitude)
                    if (gnssMarker == null) {
                        gnssMarker = m.addMarker(MarkerOptions()
                            .position(gnssPos)
                            .title("GNSS")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
                    } else {
                        gnssMarker?.position = gnssPos
                    }
                    if (isFirstFix) {
                        m.moveCamera(CameraUpdateFactory.newLatLngZoom(gnssPos, 18f))
                        isFirstFix = false
                    }
                }

                // Update PDR marker (orange)
                val pdrPts = TrajectoryRepository.pdrTrajectory.value.points
                if (pdrPts.isNotEmpty()) {
                    val pdrPos = LatLng(pdrPts.last().latitude, pdrPts.last().longitude)
                    if (pdrMarker == null) {
                        pdrMarker = m.addMarker(MarkerOptions()
                            .position(pdrPos).title("PDR")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)))
                    } else { pdrMarker?.position = pdrPos }
                }

                // Update Adaptive FGO marker (green)
                val adaptPts = TrajectoryRepository.adaptiveFgoTrajectory.value.points
                if (adaptPts.isNotEmpty()) {
                    val adaptPos = LatLng(adaptPts.last().latitude, adaptPts.last().longitude)
                    if (adaptiveMarker == null) {
                        adaptiveMarker = m.addMarker(MarkerOptions()
                            .position(adaptPos).title("Adaptive FGO ⭐")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)))
                    } else { adaptiveMarker?.position = adaptPos }
                    m.animateCamera(CameraUpdateFactory.newLatLng(adaptPos))
                }

                val windowState = TrajectoryRepository.adaptiveWindowState.value
                tvStats.text = buildString {
                    if (gnss.isValid) {
                        appendLine("GNSS: ±${gnss.accuracy.toInt()}m | ${gnss.satelliteCount} sats | CN0: ${"%.1f".format(gnss.avgCn0)} dB-Hz")
                    } else appendLine("GNSS: Acquiring...")
                    appendLine("Heading: ${"%.1f".format(snapshot.heading)}°  |  Motion var: ${"%.3f".format(snapshot.motionVariance)}")
                    appendLine("Adaptive window: ${windowState.currentWindowSize}  |  GNSS quality: ${(windowState.gnssQuality * 100).toInt()}%")
                    append("● GNSS (blue)  ● PDR (orange)  ● Adaptive FGO (green)")
                }
            }
        }
    }
}
