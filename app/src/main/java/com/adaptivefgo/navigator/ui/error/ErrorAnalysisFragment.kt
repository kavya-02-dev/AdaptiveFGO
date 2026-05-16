package com.adaptivefgo.navigator.ui.error

import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.adaptivefgo.navigator.R
import com.adaptivefgo.navigator.data.AlgorithmType
import com.adaptivefgo.navigator.data.TrajectoryPoint
import com.adaptivefgo.navigator.repository.TrajectoryRepository
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ErrorAnalysisFragment : Fragment() {

    private lateinit var barChart: BarChart
    private lateinit var lineChart: LineChart
    private lateinit var tvTable: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_error_analysis, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        barChart = view.findViewById(R.id.bar_chart_rmse)
        lineChart = view.findViewById(R.id.line_chart_error)
        tvTable = view.findViewById(R.id.tv_comparison_table)
        setupCharts()
        observeData()
    }

    private fun setupCharts() {
        barChart.apply {
            description.isEnabled = false
            legend.isEnabled = true
            setDrawGridBackground(false)
            animateY(500)
        }
        lineChart.apply {
            description.text = "Position Error Over Time (m)"
            description.isEnabled = true
            legend.apply {
                isEnabled = true
                orientation = Legend.LegendOrientation.HORIZONTAL
                horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            }
            setDrawGridBackground(false)
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            combine(
                TrajectoryRepository.gnssTrajectory,
                TrajectoryRepository.kalmanTrajectory,
                TrajectoryRepository.fixedLagTrajectory,
                TrajectoryRepository.adaptiveFgoTrajectory
            ) { g, k, fl, a -> listOf(g, k, fl, a) }
                .collect { results ->
                    if (results.all { it.points.isEmpty() }) return@collect

                    // Use GNSS as reference baseline for relative comparison
                    val reference = results[0].points  // GNSS

                    val metrics = mutableMapOf<AlgorithmType, Triple<Double, Double, Double>>()

                    // Compute relative errors against GNSS baseline
                    for (result in results) {
                        if (result.points.size >= 2 && reference.size >= 2) {
                            val (rmse, mean, max) = computeRelativeError(reference, result.points)
                            metrics[result.algorithm] = Triple(rmse, mean, max)
                        }
                    }

                    updateBarChart(metrics)
                    updateLineChart(results.map { it.algorithm to it.points })
                    updateTable(metrics)
                }
        }
    }

    private fun computeRelativeError(
        ref: List<TrajectoryPoint>,
        target: List<TrajectoryPoint>
    ): Triple<Double, Double, Double> {
        val n = minOf(ref.size, target.size)
        if (n < 2) return Triple(0.0, 0.0, 0.0)

        val errors = (0 until n).map { i ->
            TrajectoryRepository.haversineDistance(
                ref[i].latitude, ref[i].longitude,
                target[i].latitude, target[i].longitude
            )
        }
        val mean = errors.average()
        val rmse = Math.sqrt(errors.map { it * it }.average())
        val max = errors.maxOrNull() ?: 0.0
        return Triple(rmse, mean, max)
    }

    private fun updateBarChart(metrics: Map<AlgorithmType, Triple<Double, Double, Double>>) {
        val entries = metrics.entries.mapIndexed { i, (_, v) ->
            BarEntry(i.toFloat(), floatArrayOf(v.first.toFloat(), v.second.toFloat(), v.third.toFloat()))
        }

        val labels = metrics.keys.map { BaseMapFragmentCompat.NAMES[it] ?: it.name }

        val dataSet = BarDataSet(entries, "Errors (m)").apply {
            colors = listOf(Color.parseColor("#F44336"), Color.parseColor("#FF9800"), Color.parseColor("#2196F3"))
            stackLabels = arrayOf("RMSE", "Mean", "Max")
        }

        barChart.data = BarData(dataSet).apply { barWidth = 0.6f }
        barChart.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChart.xAxis.labelCount = labels.size
        barChart.invalidate()
    }

    private fun updateLineChart(trajectories: List<Pair<AlgorithmType, List<TrajectoryPoint>>>) {
        val dataSets = mutableListOf<ILineDataSet>()
        val colors = mapOf(
            AlgorithmType.GNSS to Color.parseColor("#2196F3"),
            AlgorithmType.KALMAN_FILTER to Color.parseColor("#9C27B0"),
            AlgorithmType.FIXED_LAG_FGO to Color.parseColor("#00BCD4"),
            AlgorithmType.ADAPTIVE_FGO to Color.parseColor("#4CAF50")
        )

        val reference = trajectories.find { it.first == AlgorithmType.GNSS }?.second ?: return

        trajectories.filter { it.first != AlgorithmType.GNSS }.forEach { (algo, points) ->
            if (points.size < 2) return@forEach
            val n = minOf(reference.size, points.size)
            val entries = (0 until n).map { i ->
                val dist = TrajectoryRepository.haversineDistance(
                    reference[i].latitude, reference[i].longitude,
                    points[i].latitude, points[i].longitude
                )
                Entry(i.toFloat(), dist.toFloat())
            }

            val ds = LineDataSet(entries, BaseMapFragmentCompat.NAMES[algo] ?: algo.name).apply {
                color = colors[algo] ?: Color.GRAY
                lineWidth = 2f
                setDrawCircles(false)
                setDrawValues(false)
            }
            dataSets.add(ds)
        }

        lineChart.data = LineData(dataSets)
        lineChart.invalidate()
    }

    private fun updateTable(metrics: Map<AlgorithmType, Triple<Double, Double, Double>>) {
        tvTable.text = buildString {
            appendLine("Algorithm            RMSE    Mean    Max")
            appendLine("─────────────────────────────────────────")
            metrics.forEach { (algo, m) ->
                val name = (BaseMapFragmentCompat.NAMES[algo] ?: algo.name).padEnd(20)
                appendLine("$name ${"%.2f".format(m.first)}m  ${"%.2f".format(m.second)}m  ${"%.2f".format(m.third)}m")
            }
        }
    }
}

// Helper to access constants from BaseMapFragment in different package
private object BaseMapFragmentCompat {
    val NAMES = mapOf(
        AlgorithmType.GNSS to "GNSS",
        AlgorithmType.PDR to "PDR",
        AlgorithmType.KALMAN_FILTER to "Kalman Filter",
        AlgorithmType.BATCH_FGO to "Batch FGO",
        AlgorithmType.FIXED_LAG_FGO to "Fixed-Lag FGO",
        AlgorithmType.ADAPTIVE_FGO to "Adaptive FGO"
    )
}
