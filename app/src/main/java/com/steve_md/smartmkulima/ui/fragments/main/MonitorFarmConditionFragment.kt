package com.steve_md.smartmkulima.ui.fragments.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MonitorFarmConditionFragment : Fragment(),OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private var LOCATION_PERMISSION_REQUEST_CODE = 1

    private lateinit var locationProvider: LocationProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_monitor_farm_condition, container, false)

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

        locationProvider = LocationProvider(this.requireContext())

        // Request user's location
        locationProvider.getLastKnownLocation { location ->
            if (location != null) {
                monitorFarmConditions(location.latitude, location.longitude)
            } else {
                // Handle case when location is null
                Timber.e("An error occurred")
            }
        }
    }

    private fun monitorFarmConditions(latitude: Double, longitude: Double) {
        val mockFarmConditions = FarmConditions(
            temperature = 22.0,
            humidity = 69.7,
            soilMoisture = 0.6
        )
        // Process the farm conditions data as needed
        displayFarmConditions(mockFarmConditions)
    }

    private fun displayFarmConditions(mockFarmConditions: FarmConditions) {
        // Update Farmers UI
        // Calling all Views
        val temp = view?.findViewById<TextView>(R.id.textViewTemperature)
        val humidity = view?.findViewById<TextView>(R.id.textViewHumidity)
        val soilMoisture = view?.findViewById<TextView>(R.id.textViewSoilMoisture)

        temp?.text = mockFarmConditions.temperature.toString()
        humidity?.text = mockFarmConditions.humidity.toString()
        soilMoisture?.text = mockFarmConditions.soilMoisture.toString()
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0

        // Check Map Location Permission
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        } else {
            // Request location permission
            requestLocationPermission()
        }
        val location = LatLng(0.5699258, 34.5566803)
        googleMap.addMarker(MarkerOptions().position(location).title("Your Farm is Here"))
            ?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f))
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    /**
     * Handle lifecycle for the Map
     * This is implemented in order for it ot work well
     */
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
}
