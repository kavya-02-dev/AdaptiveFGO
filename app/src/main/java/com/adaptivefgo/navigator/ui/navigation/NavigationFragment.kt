package com.adaptivefgo.navigator.ui.navigation

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.adaptivefgo.navigator.R
import com.adaptivefgo.navigator.data.AlgorithmType
import com.adaptivefgo.navigator.data.NavigationState
import com.adaptivefgo.navigator.repository.TrajectoryRepository
import com.adaptivefgo.navigator.ui.BaseMapFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NavigationFragment : BaseMapFragment() {

    private lateinit var etDestination: EditText
    private lateinit var btnNavigate: Button
    private lateinit var btnStop: Button
    private lateinit var tvNavStatus: TextView
    private var destMarker: Marker? = null
    private var userMarker: Marker? = null
    private var isNavigating = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_navigation, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        etDestination = view.findViewById(R.id.et_destination)
        btnNavigate = view.findViewById(R.id.btn_navigate)
        btnStop = view.findViewById(R.id.btn_stop_navigation)
        tvNavStatus = view.findViewById(R.id.tv_nav_status)
        view.findViewById<TextView>(R.id.tv_title).text = "Navigation"

        btnNavigate.setOnClickListener { startNavigation() }
        btnStop.setOnClickListener { stopNavigation() }

        initMapView(view, R.id.map_view, savedInstanceState)
    }

    override fun onMapReady(map: GoogleMap) {
        // Long press to set destination
        map.setOnMapLongClickListener { latLng ->
            destMarker?.remove()
            destMarker = map.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Destination")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            )
            TrajectoryRepository.updateNavigationState(
                NavigationState(
                    destinationLat = latLng.latitude,
                    destinationLon = latLng.longitude,
                    isNavigating = false
                )
            )
            tvNavStatus.text = "Destination set. Tap Navigate to start."
        }

        observePosition()
    }

    private fun startNavigation() {
        val navState = TrajectoryRepository.navigationState.value
        if (navState.destinationLat == 0.0) {
            tvNavStatus.text = "Long-press on map to set destination first"
            return
        }
        isNavigating = true
        TrajectoryRepository.updateNavigationState(navState.copy(isNavigating = true))
        btnNavigate.isEnabled = false
        btnStop.isEnabled = true
        tvNavStatus.text = "Navigating to destination using Adaptive FGO..."
    }

    private fun stopNavigation() {
        isNavigating = false
        val navState = TrajectoryRepository.navigationState.value
        TrajectoryRepository.updateNavigationState(navState.copy(isNavigating = false))
        btnNavigate.isEnabled = true
        btnStop.isEnabled = false
        tvNavStatus.text = "Navigation stopped"
    }

    private fun observePosition() {
        lifecycleScope.launch {
            TrajectoryRepository.adaptiveFgoTrajectory.collectLatest { result ->
                val m = googleMap ?: return@collectLatest
                val points = result.points
                if (points.isEmpty()) return@collectLatest

                val last = points.last()
                val userPos = LatLng(last.latitude, last.longitude)

                if (userMarker == null) {
                    userMarker = m.addMarker(
                        MarkerOptions()
                            .position(userPos)
                            .title("You (Adaptive FGO)")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                    )
                } else {
                    userMarker?.position = userPos
                }

                if (isNavigating) {
                    val navState = TrajectoryRepository.navigationState.value
                    val dist = TrajectoryRepository.haversineDistance(
                        last.latitude, last.longitude,
                        navState.destinationLat, navState.destinationLon
                    )
                    val etaMin = (dist / (1.4 * 60)).toInt() + 1  // 1.4 m/s walking speed

                    tvNavStatus.text = buildString {
                        appendLine("Navigating... ${"%.0f".format(dist)}m remaining (~${etaMin} min)")
                        append("Tracking via Adaptive FGO (window=${TrajectoryRepository.adaptiveWindowState.value.currentWindowSize})")
                    }

                    if (dist < 10) {
                        tvNavStatus.text = "✓ Arrived at destination!"
                        stopNavigation()
                    }

                    m.animateCamera(CameraUpdateFactory.newLatLng(userPos))
                }
            }
        }
    }
}
