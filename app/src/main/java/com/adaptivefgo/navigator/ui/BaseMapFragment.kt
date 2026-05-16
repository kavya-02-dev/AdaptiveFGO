package com.adaptivefgo.navigator.ui

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.adaptivefgo.navigator.data.AlgorithmType
import com.adaptivefgo.navigator.data.TrajectoryPoint
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.launch

/**
 * Base class for all map-based fragment pages.
 * Handles MapView lifecycle and provides helper methods for drawing trajectories.
 */
abstract class BaseMapFragment : Fragment() {

    protected var googleMap: GoogleMap? = null
    private var mapView: MapView? = null

    // Per-algorithm colors (consistent across all pages)
    companion object {
        val COLORS = mapOf(
            AlgorithmType.GNSS to Color.parseColor("#2196F3"),          // Blue
            AlgorithmType.PDR to Color.parseColor("#FF9800"),           // Orange
            AlgorithmType.KALMAN_FILTER to Color.parseColor("#9C27B0"), // Purple
            AlgorithmType.BATCH_FGO to Color.parseColor("#F44336"),     // Red
            AlgorithmType.FIXED_LAG_FGO to Color.parseColor("#00BCD4"), // Cyan
            AlgorithmType.ADAPTIVE_FGO to Color.parseColor("#4CAF50")   // Green
        )

        val NAMES = mapOf(
            AlgorithmType.GNSS to "GNSS",
            AlgorithmType.PDR to "PDR",
            AlgorithmType.KALMAN_FILTER to "Kalman Filter",
            AlgorithmType.BATCH_FGO to "Batch FGO",
            AlgorithmType.FIXED_LAG_FGO to "Fixed-Lag FGO",
            AlgorithmType.ADAPTIVE_FGO to "Adaptive FGO"
        )
    }

    protected fun initMapView(view: View, mapViewId: Int, savedInstanceState: Bundle?) {
        mapView = view.findViewById(mapViewId)
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { map ->
            googleMap = map
            setupMap(map)
            onMapReady(map)
        }
    }

    private fun setupMap(map: GoogleMap) {
        map.uiSettings.apply {
            isZoomControlsEnabled = true
            isCompassEnabled = true
            isMyLocationButtonEnabled = false
        }
        map.mapType = GoogleMap.MAP_TYPE_NORMAL
    }

    abstract fun onMapReady(map: GoogleMap)

    // ─── Drawing Helpers ──────────────────────────────────────────────────────

    protected fun drawTrajectory(
        map: GoogleMap,
        points: List<TrajectoryPoint>,
        color: Int,
        width: Float = 5f
    ): Polyline? {
        if (points.size < 2) return null
        val options = PolylineOptions()
            .addAll(points.map { LatLng(it.latitude, it.longitude) })
            .color(color)
            .width(width)
            .geodesic(true)
        return map.addPolyline(options)
    }

    protected fun addStartMarker(map: GoogleMap, point: TrajectoryPoint): Marker? {
        return map.addMarker(
            MarkerOptions()
                .position(LatLng(point.latitude, point.longitude))
                .title("Start")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        )
    }

    protected fun addEndMarker(map: GoogleMap, point: TrajectoryPoint): Marker? {
        return map.addMarker(
            MarkerOptions()
                .position(LatLng(point.latitude, point.longitude))
                .title("Current Position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        )
    }

    protected fun centerOnTrajectory(map: GoogleMap, points: List<TrajectoryPoint>) {
        if (points.isEmpty()) return
        val last = points.last()
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(last.latitude, last.longitude), 18f))
    }

    protected fun fitBoundsToTrajectory(map: GoogleMap, points: List<TrajectoryPoint>) {
        if (points.size < 2) return
        val builder = LatLngBounds.Builder()
        points.forEach { builder.include(LatLng(it.latitude, it.longitude)) }
        val bounds = builder.build()
        try {
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        } catch (e: Exception) {
            centerOnTrajectory(map, points)
        }
    }

    // ─── MapView Lifecycle ────────────────────────────────────────────────────

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroyView() {
        mapView?.onDestroy()
        super.onDestroyView()
    }
}
