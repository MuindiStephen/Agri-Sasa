package com.steve_md.smartmkulima.ui.fragments.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.steve_md.smartmkulima.databinding.FragmentIoTGraphsBinding
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IoTGraphsFragment : Fragment() {

    private lateinit var binding: FragmentIoTGraphsBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIoTGraphsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
        setUpIoTGraphs()
        initBinding()
    }

    private fun initBinding() {

        binding.materialToolbarioTGraphs.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpIoTGraphs() {

        lifecycleScope.launch {
            viewModel.fetchAllUbiBotData().observe(viewLifecycleOwner) { data ->
                updateChart(
                    binding.lineChartTemperature,
                    data.mapIndexed { index, it ->
                        Entry(
                            index.toFloat(),
                            it.field1Temperature.toFloat()
                        )
                    },
                    "Temperature",
                    Color.RED
                )
                updateChart(
                    binding.lineChartHumidity,
                    data.mapIndexed { index, it ->
                        Entry(
                            index.toFloat(),
                            it.field2Humidity.toFloat()
                        )
                    },
                    "Humidity",
                    Color.BLUE
                )
                updateChart(
                    binding.lineChartSoilMoisture,
                    data.mapIndexed { index, it ->
                        Entry(
                            index.toFloat(),
                            it.field10SoilMoisture.toFloat()
                        )
                    },
                    "Soil Moisture",
                    Color.GREEN
                )
                updateChart(
                    binding.lineChartSoilTemperature,
                    data.mapIndexed { index, it ->
                        Entry(
                            index.toFloat(),
                            it.field9SoilTemperature.toFloat()
                        )
                    },
                    "Soil Temperature",
                    Color.MAGENTA
                )
                updateChart(
                    binding.lineChartLightDensity,
                    data.mapIndexed { index, it ->
                        Entry(
                            index.toFloat(),
                            it.field6Light.toFloat()
                        )
                    },
                    "Light",
                    Color.YELLOW
                )
            }
        }
    }


    private fun updateChart(chart: LineChart, entries: List<Entry>, label: String, lineColor: Int) {
        val dataSet = LineDataSet(entries, label).apply {
            color = lineColor
            valueTextColor = Color.BLACK
            lineWidth = 2f
            setCircleColor(lineColor)
            circleRadius = 4f
            setDrawCircles(true)
            setDrawValues(false)
        }

        chart.apply {
            data = LineData(dataSet)
            description.isEnabled = false
            setTouchEnabled(true)
            setPinchZoom(true)
            axisLeft.isEnabled = true
            axisRight.isEnabled = false
            xAxis.isEnabled = true
            legend.isEnabled = true
            invalidate() // Refresh chart
        }
    }
}
