package com.adaptivefgo.navigator.ui.settings

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.adaptivefgo.navigator.R
import com.adaptivefgo.navigator.data.AlgorithmSettings
import com.adaptivefgo.navigator.repository.TrajectoryRepository

class SettingsFragment : Fragment() {

    private lateinit var tvStepLengthError: TextView
    private lateinit var sbStepLengthError: SeekBar
    private lateinit var tvHeadingNoise: TextView
    private lateinit var sbHeadingNoise: SeekBar
    private lateinit var tvGnssNoise: TextView
    private lateinit var sbGnssNoise: SeekBar
    private lateinit var tvMinWindow: TextView
    private lateinit var sbMinWindow: SeekBar
    private lateinit var tvMaxWindow: TextView
    private lateinit var sbMaxWindow: SeekBar
    private lateinit var tvAdaptiveSens: TextView
    private lateinit var sbAdaptiveSens: SeekBar
    private lateinit var btnApply: Button
    private lateinit var btnReset: Button
    private lateinit var btnResetTrajectory: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_settings, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvStepLengthError = view.findViewById(R.id.tv_step_length_error)
        sbStepLengthError = view.findViewById(R.id.sb_step_length_error)
        tvHeadingNoise = view.findViewById(R.id.tv_heading_noise)
        sbHeadingNoise = view.findViewById(R.id.sb_heading_noise)
        tvGnssNoise = view.findViewById(R.id.tv_gnss_noise)
        sbGnssNoise = view.findViewById(R.id.sb_gnss_noise)
        tvMinWindow = view.findViewById(R.id.tv_min_window)
        sbMinWindow = view.findViewById(R.id.sb_min_window)
        tvMaxWindow = view.findViewById(R.id.tv_max_window)
        sbMaxWindow = view.findViewById(R.id.sb_max_window)
        tvAdaptiveSens = view.findViewById(R.id.tv_adaptive_sens)
        sbAdaptiveSens = view.findViewById(R.id.sb_adaptive_sens)
        btnApply = view.findViewById(R.id.btn_apply_settings)
        btnReset = view.findViewById(R.id.btn_reset_defaults)
        btnResetTrajectory = view.findViewById(R.id.btn_reset_trajectory)

        loadCurrentSettings()
        setupListeners()
    }

    private fun loadCurrentSettings() {
        val s = TrajectoryRepository.settings.value
        sbStepLengthError.progress = (s.stepLengthError * 100).toInt()
        sbHeadingNoise.progress = s.headingNoise.toInt()
        sbGnssNoise.progress = s.gnssNoise.toInt()
        sbMinWindow.progress = s.minWindowSize
        sbMaxWindow.progress = s.maxWindowSize
        sbAdaptiveSens.progress = (s.adaptiveSensitivity * 50).toInt()
        updateLabels(s)
    }

    private fun setupListeners() {
        val listener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(sb: SeekBar, progress: Int, fromUser: Boolean) {
                updateLabels(buildSettings())
            }
            override fun onStartTrackingTouch(sb: SeekBar?) {}
            override fun onStopTrackingTouch(sb: SeekBar?) {}
        }

        sbStepLengthError.setOnSeekBarChangeListener(listener)
        sbHeadingNoise.setOnSeekBarChangeListener(listener)
        sbGnssNoise.setOnSeekBarChangeListener(listener)
        sbMinWindow.setOnSeekBarChangeListener(listener)
        sbMaxWindow.setOnSeekBarChangeListener(listener)
        sbAdaptiveSens.setOnSeekBarChangeListener(listener)

        btnApply.setOnClickListener {
            val settings = buildSettings()
            TrajectoryRepository.updateSettings(settings)
            btnApply.text = "✓ Applied!"
            btnApply.postDelayed({ btnApply.text = "Apply Settings" }, 1500)
        }

        btnReset.setOnClickListener {
            val defaults = AlgorithmSettings()
            TrajectoryRepository.updateSettings(defaults)
            loadCurrentSettings()
        }

        btnResetTrajectory.setOnClickListener {
            TrajectoryRepository.reset()
            btnResetTrajectory.text = "✓ Reset!"
            btnResetTrajectory.postDelayed({ btnResetTrajectory.text = "Reset All Trajectories" }, 1500)
        }
    }

    private fun buildSettings(): AlgorithmSettings {
        return AlgorithmSettings(
            stepLengthError = sbStepLengthError.progress / 100f,
            headingNoise = sbHeadingNoise.progress.toFloat().coerceAtLeast(1f),
            gnssNoise = sbGnssNoise.progress.toFloat().coerceAtLeast(1f),
            minWindowSize = sbMinWindow.progress.coerceAtLeast(3),
            maxWindowSize = sbMaxWindow.progress.coerceAtLeast(sbMinWindow.progress + 5),
            adaptiveSensitivity = sbAdaptiveSens.progress / 50f
        )
    }

    private fun updateLabels(s: AlgorithmSettings) {
        tvStepLengthError.text = "Step Length Error: ±${(s.stepLengthError * 100).toInt()}cm"
        tvHeadingNoise.text = "Heading Noise: ±${s.headingNoise.toInt()}°"
        tvGnssNoise.text = "GNSS Noise: ±${s.gnssNoise.toInt()}m"
        tvMinWindow.text = "Min Window Size: ${s.minWindowSize}"
        tvMaxWindow.text = "Max Window Size: ${s.maxWindowSize}"
        tvAdaptiveSens.text = "Adaptive Sensitivity: ${"%.2f".format(s.adaptiveSensitivity)}x"
    }
}
