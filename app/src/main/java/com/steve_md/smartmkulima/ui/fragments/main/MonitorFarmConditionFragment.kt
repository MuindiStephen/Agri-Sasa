package com.steve_md.smartmkulima.ui.fragments.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.model.FarmConditions
import com.steve_md.smartmkulima.ui.fragments.others.LocationProvider
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.isInternetAvailable
import dagger.hilt.android.AndroidEntryPoint
import ir.mahozad.android.PieChart
import ir.mahozad.android.unit.Dimension
import kotlinx.coroutines.flow.combine
import timber.log.Timber

/**
 * Monitor Farm conditions + (GPS locator)
 */
@AndroidEntryPoint
class MonitorFarmConditionFragment : Fragment(),OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var LOCATIONPERMISSIONREQUESTCODE = 1
    private lateinit var locationProvider: LocationProvider


   // private val args: MonitorFarmConditionFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_monitor_farm_condition,
            container,
            false
        )

        val backIcon = view.findViewById<ImageView>(R.id.imageView11)
        backIcon.setOnClickListener { findNavController().navigateUp() }

        mapView = view.findViewById(R.id.mapViewFarmMonitoring)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()

        initBinding()

        if (!isLocationEnabled()) {
            promptEnableLocationServices()
            return
        }
        
        locationProvider = LocationProvider(this.requireContext())

        // Request user's location
        locationProvider.getLastKnownLocation { location ->

            if (location == null) {
                displaySnackBar("Unable to get current location. Please check your device settings")
                return@getLastKnownLocation
            } else {
                Timber.tag("LocateMyFarm").e("Location services are enabled. Showing farm location")
            }

            val loading = view.findViewById<ProgressBar>(R.id.progressBar8)
            loading.isVisible = true

            if (location != null) {
                loading.isVisible = false
                monitorFarmConditions(location.latitude, location.longitude)
            } else {
                // Handle case when location is null
                loading.isVisible = false
                Timber.e("An error occurred")
                displaySnackBar("Could not update your location")
            }
        }

        setUpChart()
    }

    private fun initBinding() {
        view?.findViewById<TextView>(R.id.textViewViewAllGraphs)
            ?.setOnClickListener {
                findNavController().navigate(R.id.ioTGraphsFragment)
            }
    }


    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    // Prompt user to enable location if it’s turned off
    private fun promptEnableLocationServices() {
        AlertDialog.Builder(requireContext())
            .setTitle("Enable Location Services")
            .setMessage("Location services are required to locate your farm. Please enable them.")
            .setPositiveButton("Settings") { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun setUpChart() {
        val pieChart = view?.findViewById<PieChart>(R.id.pieChart)

        pieChart?.slices = listOf(
            PieChart.Slice(0.20f, Color.rgb(120, 181, 0), Color.rgb(149, 224, 0), legend = "Temperature"),
            PieChart.Slice(0.30f, Color.rgb(204, 168, 0), Color.rgb(249, 228, 0), legend = "Humidity"),
            PieChart.Slice(0.18f, Color.rgb(0, 162, 216), Color.rgb(31, 199, 255), legend = "Soil moisture"),
            PieChart.Slice(0.12f, Color.rgb(255, 4, 4), Color.rgb(255, 72, 86), legend = "Light density"),
            PieChart.Slice(0.14f, Color.rgb(160, 100, 167), Color.rgb(160, 145, 175), legend = "Soil temperature") ,
        )


        pieChart?.gradientType = PieChart.GradientType.RADIAL
        pieChart?.legendIconsMargin = Dimension.DP(8F)
        pieChart?.legendTitleMargin = Dimension.DP(14F)
        pieChart?.legendLinesMargin = Dimension.DP(10F)
        pieChart?.legendsMargin = Dimension.DP(20F)
        pieChart?.legendsPercentageMargin = Dimension.DP(8F)
        pieChart?.legendsSize = Dimension.DP(11F)
        pieChart?.legendsPercentageSize = Dimension.DP(11F)
        pieChart?.legendsIcon = PieChart.DefaultIcons.SQUARE
        pieChart?.legendsTitle = "Farm conditions"
        pieChart?.legendPosition = PieChart.LegendPosition.BOTTOM
        pieChart?.labelType = PieChart.LabelType.NONE
        pieChart?.isLegendEnabled = true
        pieChart?.legendsTitleColor = Color.BLACK
        pieChart?.legendsColor = Color.BLACK

//        pieChart?.isAnimationEnabled = true
//        pieChart?.isLegendsPercentageEnabled = true
    }

    val Float.toSp get() = this * Resources.getSystem().displayMetrics.scaledDensity

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }
    // Mock farm conditions with Fake DATA
    private fun monitorFarmConditions(latitude: Double, longitude: Double) {
        val mockFarmConditions = FarmConditions(
            temperature = 21.448463,
            humidity = 69.0,
            soilMoisture = 28.6,
            windspeed = 0.0,
            precipitation = 0.0,
            lightDensity = 2.9,
            nbkLevel = 0.0,
            soilPh = 0.0,
            soilTemperature = 18.4
        )

        /*
            val temperature: Double,
            val humidity: Double,
            val soilMoisture: Double,
            val windspeed: Double,
            val precipitation: Double,
            val lightDensity: Double,
            val nbkLevel: Double
         */
        // Process the farm conditions data as needed
        displayFarmConditions(mockFarmConditions)
    }

    @SuppressLint("SetTextI18n")
    private fun displayFarmConditions(mockFarmConditions: FarmConditions) {


        /*
         val mockFarmConditions = FarmConditions(
            temperature = 21.448463,
            humidity = 69.0,
            soilMoisture = 28.6,
            windspeed = 0.0,
            precipitation = 0.0,
            lightDensity = 2.9,
            nbkLevel = 0.0,
            soilPh = 0.0,
            soilTemperature = 18.4
        )
         */

        val temperature = view?.findViewById<TextView>(R.id.textViewTemperature)
        val humidity = view?.findViewById<TextView>(R.id.textViewHumidity)
        val soilMoisture = view?.findViewById<TextView>(R.id.textViewSoilMoisture)
        val windspeed = view?.findViewById<TextView>(R.id.textViewWindSpeed)
        val precipitation = view?.findViewById<TextView>(R.id.textViewPrecipitation)
        val lightDensity = view?.findViewById<TextView>(R.id.textViewLightDensity)
        val nbkLevel = view?.findViewById<TextView>(R.id.textViewNBKLevel)

        // As kotlin does not interpret html symbol entities
        temperature?.text = "${mockFarmConditions.temperature}°C"
        humidity?.text = "${mockFarmConditions.humidity}%"
        soilMoisture?.text = "${mockFarmConditions.soilMoisture}%"
        windspeed?.text = "${mockFarmConditions.windspeed} m/s"
        precipitation?.text = "${mockFarmConditions.precipitation} mm"
        lightDensity?.text = "${mockFarmConditions.lightDensity}"
        nbkLevel?.text = "${mockFarmConditions.nbkLevel} %"
        view?.findViewById<TextView>(R.id.textViewSoilTemp)?.text = "${mockFarmConditions.soilTemperature}"
    }

    /**
     * If we can add a graph to show the IoT reading in realtime
     */

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        // Handle No internet connection when map is not loaded
        if (!isInternetAvailable(requireContext())) {
            showInternetErrorDialog()
            return
        } else {
            displaySnackBar("Farm Location update successful")
        }

        // Check Map Location Permission
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }

        // Sample farm Location // Not Real Data
        val location = LatLng(-1.2860732, 36.8103714)

        googleMap.addMarker(MarkerOptions().position(location).title("Your Farm is Here"))
            ?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATIONPERMISSIONREQUESTCODE
        )
    }

    /**
     * Handle lifecycle for the Map
     * This is implemented in order for the map to work pretty well
     */
    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }
    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    /**
     * Handle null map-location
     */
    private fun showInternetErrorDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("No Internet Connection to load map")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton("Retry") { dialog, _ ->
                dialog.dismiss()
                // Retry the map load or any other action
                mapView.getMapAsync(this@MonitorFarmConditionFragment)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}
