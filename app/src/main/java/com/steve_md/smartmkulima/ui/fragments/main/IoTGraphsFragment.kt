package com.steve_md.smartmkulima.ui.fragments.main

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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
    }

    private fun setUpSoilTemperature() {


        val soilTemperatureData = listOf(
            20.1, 20, 19.79999, 19.7, 19.79,
            19.9, 20, 20.1, 19.6, 18.6, 19.1,
            19.20001, 19, 18.9, 19.29999, 18.4
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
            0.06, 0.06, 0.06, 0.02, 0.04,
            0.01, 0.0,0.0,0.06, 0.07, 0.01,
            0.14, 0.13, 0.14, 0.01, 0.0,
            0.0, 0.0, 0.06, 0.07, 0.0,
            0.01, 0.14, 0.13, 0.0, 0.5, 22.91, 23.91, 24,2999
            ,25,132,178, 320, 480,130,33, 770,5208,
            12300,38462, 14000, 10000, 2000,
            900,17,0.21, 5.92, 1000, 5000, 80000,
            10000, 4000, 2900, 900,
            200, 129, 35, 6.34, 3.15, 2.65, 3.26, 2.9
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
            3.2, 3.1, 3.2,
            0, 50, 12.1,
            11.5, 10.9, 9.5,
            35.59998, 29.9, 29, 28.6
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
            79, 80, 81, 82, 66, 65, 67,
            47, 48, 50, 56, 41, 36, 35,
            34, 45, 70, 79, 64,
            76, 80, 93, 71, 70, 69
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
            19.258797, 19.090561, 19.021133, 18.978409,
            18.99176, 18.965057, 18.965057, 18.922333,
            18.951706, 18.99176, 18.922333, 19.007782,
            18.852901, 18.794155, 18.764782, 18.810177,
            18.852901, 18.852901, 18.908981, 18.951706,
            11.894028, 21.448463
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