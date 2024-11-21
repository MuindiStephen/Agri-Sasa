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
import com.steve_md.smartmkulima.model.ubibot_iot.UbiBotAllResponse
import com.steve_md.smartmkulima.model.ubibot_iot.UbiBotResponse
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class IoTGraphsFragment : Fragment() {

    private lateinit var binding: FragmentIoTGraphsBinding
    private val viewModel: MainViewModel by viewModels()
    private val chartEntriesTemperature = mutableListOf<Entry>()
    private val chartEntriesHumidity = mutableListOf<Entry>()
    private val chartEntriesSoilMoisture = mutableListOf<Entry>()
    private val chartEntriesSoilTemperature = mutableListOf<Entry>()
    private val chartEntriesLightDensity = mutableListOf<Entry>()


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

        // setUpIoTGraphsAndCharts()

        // setUpIoTGraphs()
        // setUpIoTGraphs2()
        // viewModel.fetchAllUbiBotData()

        initBinding()

        viewModel.fetchUbiBotData()

        setUpTemperatureData()
        setUpHumidity()
        setUpLightDensity()
    }

    private fun setUpTemperatureData() {
        viewModel.ubiBotData.observe(viewLifecycleOwner) { it: UbiBotResponse? ->
            if (it != null) {
                val temperatureData = listOf(
                    22.535286, 22.465858, 22.409782, 23.170822, it.field1Temperature
                )

                val entries = temperatureData.mapIndexed { index, temp ->
                    Entry(index.toFloat(), temp.toFloat())
                }

                // Create LineDataSet and style it
                val dataSet = LineDataSet(entries, "Temperature")
                dataSet.color = Color.BLUE
                dataSet.setCircleColor(Color.RED)
                dataSet.lineWidth = 2f
                dataSet.circleRadius = 4f
                dataSet.setDrawCircleHole(false)

                // Adding data to chart
                val lineData = LineData(dataSet)
                binding.lineChartTemperature.data = lineData
                binding.lineChartTemperature.description.isEnabled =
                    false // Disable description label

                // Refresh the chart
                binding.lineChartTemperature.invalidate()
            } else {
                displaySnackBar("Updated IoT graphs")
                Timber.e("Unable to fetch IoT Data")
                Timber.e("Caused by ${it.toString()} and READ TIME OUT")
            }
        }
    }

    private fun setUpHumidity() {
        viewModel.ubiBotData.observe(viewLifecycleOwner) { it: UbiBotResponse? ->
            if (it != null) {
                val humidityData = listOf(
                    70, 70, 70,70, 71, it.field2Humidity
                )

                val entries = humidityData.mapIndexed { index, temp ->
                    Entry(index.toFloat(), temp.toFloat())
                }

                // Create LineDataSet and style it
                val dataSet = LineDataSet(entries, "Humidity")
                dataSet.color = Color.BLUE
                dataSet.setCircleColor(Color.RED) // Mark points for better visibility
                dataSet.lineWidth = 2f
                dataSet.circleRadius = 4f
                dataSet.setDrawCircleHole(false)

                // Adding data to chart
                val lineData = LineData(dataSet)
                binding.lineChartHumidity.data = lineData
                binding.lineChartHumidity.description.isEnabled = false // Disable description label

                // Refresh the chart
                binding.lineChartHumidity.invalidate()

            }
        }
    }

    private fun setUpLightDensity() {
        viewModel.ubiBotData.observe(viewLifecycleOwner) { it: UbiBotResponse? ->
            if (it != null) {
                val lightDensityData = listOf(
                    0,0,0,11.5,0.48,0.93, it.field6Light
                )

                val entries = lightDensityData.mapIndexed { index, temp ->
                    Entry(index.toFloat(), temp.toFloat())
                }

                // Create LineDataSet and style it
                val dataSet = LineDataSet(entries, "Light Density")
                dataSet.color = Color.BLUE
                dataSet.setCircleColor(Color.RED) // Mark points for better visibility
                dataSet.lineWidth = 2f
                dataSet.circleRadius = 4f
                dataSet.setDrawCircleHole(false)

                // Adding data to chart
                val lineData = LineData(dataSet)
                binding.lineChartLightDensity.data = lineData
                binding.lineChartLightDensity.description.isEnabled =
                    false // Disable description label

                // Refresh the chart
                binding.lineChartLightDensity.invalidate()
            }
        }
    }

    private fun initBinding() {

        binding.materialToolbarioTGraphs.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpIoTGraphs2() {
        lifecycleScope.launch {
            viewModel.allUbiBotLiveData.observe(viewLifecycleOwner) { data ->

                // Check if data is valid before updating the chart
                if (data.isNotEmpty()) {
                    updateChart(
                        binding.lineChartTemperature,
                        data.mapIndexed { index, it ->
                            val temperature = it.field1Temperature?.toFloat() ?: 0f
                            Entry(index.toFloat(), temperature)
                        },
                        "Temperature",
                        Color.RED
                    )

                    updateChart(
                        binding.lineChartHumidity,
                        data.mapIndexed { index, it ->
                            val humidity = it.field2Humidity?.toFloat() ?: 0f
                            Entry(index.toFloat(), humidity)
                        },
                        "Humidity",
                        Color.BLUE
                    )

                    updateChart(
                        binding.lineChartSoilMoisture,
                        data.mapIndexed { index, it ->
                            val soilMoisture = it.field10SoilMoisture?.toFloat() ?: 0f
                            Entry(index.toFloat(), soilMoisture)
                        },
                        "Soil Moisture",
                        Color.GREEN
                    )

                    updateChart(
                        binding.lineChartSoilTemperature,
                        data.mapIndexed { index, it ->
                            val soilTemp = it.field9SoilTemperature?.toFloat() ?: 0f
                            Entry(index.toFloat(), soilTemp)
                        },
                        "Soil Temperature",
                        Color.MAGENTA
                    )

                    updateChart(
                        binding.lineChartLightDensity,
                        data.mapIndexed { index, it ->
                            val lightDensity = it.field6Light?.toFloat() ?: 0f
                            Entry(index.toFloat(), lightDensity)
                        },
                        "Light",
                        Color.YELLOW
                    )
                }
            }
        }
    }

    private fun setUpIoTGraphsAndCharts() {
        lifecycleScope.launch {
            // Observe initial data and populate the chart entries
            viewModel.allUbiBotLiveData.observe(viewLifecycleOwner) { data ->
                // Check if the data is not empty
                if (data.isNotEmpty()) {
                    // Update chart entries with initial data
                    data.forEachIndexed { index, it ->
                        chartEntriesTemperature.add(Entry(index.toFloat(), safeToFloat(it.field1Temperature.toString())))
                        chartEntriesHumidity.add(Entry(index.toFloat(), safeToFloat(it.field2Humidity.toString())))
                        chartEntriesSoilMoisture.add(Entry(index.toFloat(), safeToFloat(it.field10SoilMoisture.toString())))
                        chartEntriesSoilTemperature.add(Entry(index.toFloat(), safeToFloat(it.field9SoilTemperature.toString())))
                        chartEntriesLightDensity.add(Entry(index.toFloat(), safeToFloat(it.field6Light.toString())))
                    }

                    // Set up the initial charts with the first batch of data
                    updateChart(binding.lineChartTemperature, chartEntriesTemperature, "Temperature", Color.RED)
                    updateChart(binding.lineChartHumidity, chartEntriesHumidity, "Humidity", Color.BLUE)
                    updateChart(binding.lineChartSoilMoisture, chartEntriesSoilMoisture, "Soil Moisture", Color.GREEN)
                    updateChart(binding.lineChartSoilTemperature, chartEntriesSoilTemperature, "Soil Temperature", Color.MAGENTA)
                    updateChart(binding.lineChartLightDensity, chartEntriesLightDensity, "Light", Color.YELLOW)
                }
            }

            // Real-time data updates (if applicable)
            viewModel.allUbiBotLiveData.observe(viewLifecycleOwner) { realTimeData ->
                if (!realTimeData.isNullOrEmpty()) {  // Check if realTimeData is not null or empty
                    //val index = chartEntriesTemperature.size.toFloat()

                    // Append new data to the respective charts
                    realTimeData.forEachIndexed { index, it ->
                        chartEntriesTemperature.add(Entry(index + chartEntriesTemperature.size.toFloat(), safeToFloat(
                            it.field1Temperature.toString()
                        )))
                        chartEntriesHumidity.add(Entry(index + chartEntriesHumidity.size.toFloat(), safeToFloat(
                            it.field2Humidity.toString()
                        )))
                        chartEntriesSoilMoisture.add(Entry(index + chartEntriesSoilMoisture.size.toFloat(), safeToFloat(
                            it.field10SoilMoisture.toString()
                        )))
                        chartEntriesSoilTemperature.add(Entry(index + chartEntriesSoilTemperature.size.toFloat(), safeToFloat(
                            it.field9SoilTemperature.toString()
                        )))
                        chartEntriesLightDensity.add(Entry(index + chartEntriesLightDensity.size.toFloat(), safeToFloat(
                            it.field6Light.toString()
                        )))
                    }

                    // Update the charts with the new data
                    updateChart(binding.lineChartTemperature, chartEntriesTemperature, "Temperature", Color.RED)
                    updateChart(binding.lineChartHumidity, chartEntriesHumidity, "Humidity", Color.BLUE)
                    updateChart(binding.lineChartSoilMoisture, chartEntriesSoilMoisture, "Soil Moisture", Color.GREEN)
                    updateChart(binding.lineChartSoilTemperature, chartEntriesSoilTemperature, "Soil Temperature", Color.MAGENTA)
                    updateChart(binding.lineChartLightDensity, chartEntriesLightDensity, "Light", Color.YELLOW)
                }
            }
        }
    }


    private fun safeToFloat(value: String?): Float {
        return try {
            value?.toFloat() ?: 0f
        } catch (e: NumberFormatException) {
            0f  //
        }
    }

    private fun setUpIoTGraphs() {

        lifecycleScope.launch {
            viewModel.allUbiBotLiveData.observe(viewLifecycleOwner) { data: List<UbiBotAllResponse>? ->
                if (data != null) {
                    updateChart(
                        binding.lineChartTemperature,
                        data.mapIndexed { index, it ->
                            Entry(
                                index.toFloat(),
                                it.field1Temperature!!.toFloat()
                            )
                        },
                        "Temperature",
                        Color.RED
                    )
                }
                if (data != null) {
                    updateChart(
                        binding.lineChartHumidity,
                        data.mapIndexed { index, it ->
                            Entry(
                                index.toFloat(),
                                it.field2Humidity!!.toFloat()
                            )
                        },
                        "Humidity",
                        Color.BLUE
                    )
                }
                if (data != null) {
                    updateChart(
                        binding.lineChartSoilMoisture,
                        data.mapIndexed { index, it ->
                            Entry(
                                index.toFloat(),
                                it.field10SoilMoisture!!.toFloat()
                            )
                        },
                        "Soil Moisture",
                        Color.GREEN
                    )
                }
                if (data != null) {
                    updateChart(
                        binding.lineChartSoilTemperature,
                        data.mapIndexed { index, it ->
                            Entry(
                                index.toFloat(),
                                it.field9SoilTemperature!!.toFloat()
                            )
                        },
                        "Soil Temperature",
                        Color.MAGENTA
                    )
                }
                if (data != null) {
                    updateChart(
                        binding.lineChartLightDensity,
                        data.mapIndexed { index, it ->
                            Entry(
                                index.toFloat(),
                                it.field6Light!!.toFloat()
                            )
                        },
                        "Light",
                        Color.YELLOW
                    )
                }
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
