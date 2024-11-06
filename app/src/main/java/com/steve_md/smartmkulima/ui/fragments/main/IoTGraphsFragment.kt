package com.steve_md.smartmkulima.ui.fragments.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.steve_md.smartmkulima.databinding.FragmentIoTGraphsBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Display the line charts / graphical representation
 * of changing I.o.T Data values.
 */
@AndroidEntryPoint
class IoTGraphsFragment : Fragment() {

    private lateinit var binding: FragmentIoTGraphsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentIoTGraphsBinding.inflate(
            inflater, container, false
        )
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        setUpTemperatureData()
        setUpHumidity()
        setUpSoilMoisture()
        setUpLightDensity()
        setUpSoilTemperature()

        initBinding()
    }

    private fun initBinding() {
        binding.materialToolbarioTGraphs.setOnClickListener {
            findNavController().navigateUp()
        }
    }


    // reading only latest week / last week's data.
    private fun setUpSoilTemperature() {


        val soilTemperatureData = listOf(
            24.299999 ,23.9,23.200001,22.799999,22.299999,22.1,
            19.9, 19.1, 18.9,  18.4
        )

        val entries = soilTemperatureData.mapIndexed { index, temp ->
            Entry(index.toFloat(), temp.toFloat())
        }

        // Create LineDataSet and style it
        val dataSet = LineDataSet(entries, "Soil Temperature")
        dataSet.color = Color.BLUE
        dataSet.setCircleColor(Color.RED) // Mark points for better visibility
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 4f
        dataSet.setDrawCircleHole(false)

        // Adding data to chart
        val lineData = LineData(dataSet)
        binding.lineChartSoilTemperature.data = lineData
        binding.lineChartSoilTemperature.description.isEnabled = false // Disable description label

        // Refresh the chart
        binding.lineChartSoilTemperature.invalidate()
    }

    private fun setUpLightDensity() {
        val lightDensityData = listOf(
              3.1, 3.04, 2.93, 2.53,3.07, 3.06,
              3.09, 3.06, 3.08, 3.21, 3.47, 3.17, 3.2, 3.23,
              0, 0.05, 0.17, 0.12, 4.03, 3.03, 2.48, 9.3,
              6.34, 3.15, 2.65, 3.26, 2.9
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
        binding.lineChartLightDensity.description.isEnabled = false // Disable description label

        // Refresh the chart
        binding.lineChartLightDensity.invalidate()
    }

    private fun setUpSoilMoisture() {
        val soilMoistureData = listOf(
            11.5, 10.9, 10.5, 10.3, 10.1,
            10.1, 9.9, 9.8, 9.8, 9.6, 9.6,
            9.5, 9.5, 35.599998, 29.9, 29, 28.6
        )
        val entries = soilMoistureData.mapIndexed { index, temp ->
            Entry(index.toFloat(), temp.toFloat())
        }

        // Create LineDataSet and style it
        val dataSet = LineDataSet(entries, "Soil Moisture")
        dataSet.color = Color.BLUE
        dataSet.setCircleColor(Color.RED) // Mark points for better visibility
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 4f
        dataSet.setDrawCircleHole(false)

        // Adding data to chart
        val lineData = LineData(dataSet)
        binding.lineChartSoilMoisture.data = lineData
        binding.lineChartSoilMoisture.description.isEnabled = false // Disable description label

        // Refresh the chart
        binding.lineChartSoilMoisture.invalidate()

    }

    private fun setUpHumidity() {
        val humidityData = listOf(
            70, 70,70,70,70,70,71,72,72,72,72,
            73,72,72, 71,71, 72, 71, 71,
            72, 71,71,71, 71, 70,70, 70, 70, 69
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

    private fun setUpTemperatureData() {
        val temperatureData = listOf(
             21.010529, 21.066605, 20.770203,
             20.658043, 20.898376, 21.02388,
             21.181427,21.165405,21.194778,21.266884,
             21.250862, 21.405739, 21.3363311,
             21.435112, 21.504539, 21.448463
        )

        val entries = temperatureData.mapIndexed { index, temp ->
            Entry(index.toFloat(), temp.toFloat())
        }

        // Create LineDataSet and style it
        val dataSet = LineDataSet(entries, "Temperature")
        dataSet.color = Color.BLUE
        dataSet.setCircleColor(Color.RED) // Mark points for better visibility
        dataSet.lineWidth = 2f
        dataSet.circleRadius = 4f
        dataSet.setDrawCircleHole(false)

        // Adding data to chart
        val lineData = LineData(dataSet)
        binding.lineChartTemperature.data = lineData
        binding.lineChartTemperature.description.isEnabled = false // Disable description label

        // Refresh the chart
        binding.lineChartTemperature.invalidate()
    }
}