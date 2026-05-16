package com.adaptivefgo.navigator

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.adaptivefgo.navigator.databinding.ActivityMainBinding
import com.adaptivefgo.navigator.repository.TrajectoryRepository
import com.adaptivefgo.navigator.service.LocalizationService
import com.adaptivefgo.navigator.ui.adaptivefgo.AdaptiveFgoMapFragment
import com.adaptivefgo.navigator.ui.batchfgo.BatchFgoMapFragment
import com.adaptivefgo.navigator.ui.comparison.ComparisonMapFragment
import com.adaptivefgo.navigator.ui.error.ErrorAnalysisFragment
import com.adaptivefgo.navigator.ui.fixedlagfgo.FixedLagMapFragment
import com.adaptivefgo.navigator.ui.gnss.GnssMapFragment
import com.adaptivefgo.navigator.ui.kalman.KalmanMapFragment
import com.adaptivefgo.navigator.ui.navigation.NavigationFragment
import com.adaptivefgo.navigator.ui.pdr.PdrMapFragment
import com.adaptivefgo.navigator.ui.settings.SettingsFragment
import com.adaptivefgo.navigator.ui.tracking.RealTimeTrackingFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewPager: ViewPager2

    private val PAGE_TITLES = listOf(
        "1 GNSS", "2 PDR", "3 Kalman", "4 BatchFGO",
        "5 FixedFGO", "6 ★AdaptFGO", "7 Compare",
        "8 Error", "9 Live", "10 Navigate", "11 Settings"
    )

    private val requiredPermissions = mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
    }.toTypedArray()

    private val permissionsLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val allGranted = results.values.all { it }
        if (allGranted) {
            startLocalizationService()
        } else {
            Toast.makeText(this, "Location permission required for localization", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPager()
        setupServiceButton()
        observeServiceState()
        checkPermissionsAndStart()
    }

    private fun setupViewPager() {
        viewPager = binding.viewPager
        viewPager.adapter = NavigatorPagerAdapter(this)
        viewPager.offscreenPageLimit = 3  // Pre-render nearby pages

        TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
            tab.text = PAGE_TITLES[position]
        }.attach()
    }

    private fun setupServiceButton() {
        binding.fabService.setOnClickListener {
            if (TrajectoryRepository.isServiceRunning.value) {
                stopLocalizationService()
            } else {
                checkPermissionsAndStart()
            }
        }

        binding.fabReset.setOnClickListener {
            sendServiceCommand(LocalizationService.ACTION_RESET)
            Toast.makeText(this, "All trajectories reset", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeServiceState() {
        lifecycleScope.launch {
            TrajectoryRepository.isServiceRunning.collectLatest { running ->
                binding.fabService.setImageResource(
                    if (running) android.R.drawable.ic_media_pause
                    else android.R.drawable.ic_media_play
                )
                binding.tvServiceStatus.text = if (running) "● Recording" else "● Stopped"
                binding.tvServiceStatus.setTextColor(
                    ContextCompat.getColor(this@MainActivity,
                        if (running) android.R.color.holo_green_dark else android.R.color.holo_red_dark)
                )
            }
        }

        lifecycleScope.launch {
            TrajectoryRepository.latestSnapshot.collectLatest { snapshot ->
                if (snapshot != null) {
                    val gnss = snapshot.gnss
                    binding.tvStatusBar.text = buildString {
                        if (gnss.isValid) {
                            append("GPS: ${gnss.satelliteCount}sats ±${gnss.accuracy.toInt()}m")
                            append("  Hdg: ${snapshot.heading.toInt()}°")
                        } else {
                            append("GPS: acquiring...")
                        }
                        val wState = TrajectoryRepository.adaptiveWindowState.value
                        append("  W=${wState.currentWindowSize}")
                    }
                }
            }
        }
    }

    private fun checkPermissionsAndStart() {
        val allGranted = requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
        if (allGranted) {
            startLocalizationService()
        } else {
            permissionsLauncher.launch(requiredPermissions)
        }
    }

    private fun startLocalizationService() {
        val intent = Intent(this, LocalizationService::class.java).apply {
            action = LocalizationService.ACTION_START
        }
        startForegroundService(intent)
    }

    private fun stopLocalizationService() {
        val intent = Intent(this, LocalizationService::class.java).apply {
            action = LocalizationService.ACTION_STOP
        }
        startService(intent)
    }

    private fun sendServiceCommand(action: String) {
        val intent = Intent(this, LocalizationService::class.java).apply {
            this.action = action
        }
        startService(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Service keeps running in background
    }
}

// ─── ViewPager Adapter ────────────────────────────────────────────────────────

class NavigatorPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 11

    override fun createFragment(position: Int): Fragment = when (position) {
        0 -> GnssMapFragment()
        1 -> PdrMapFragment()
        2 -> KalmanMapFragment()
        3 -> BatchFgoMapFragment()
        4 -> FixedLagMapFragment()
        5 -> AdaptiveFgoMapFragment()
        6 -> ComparisonMapFragment()
        7 -> ErrorAnalysisFragment()
        8 -> RealTimeTrackingFragment()
        9 -> NavigationFragment()
        10 -> SettingsFragment()
        else -> GnssMapFragment()
    }
}
