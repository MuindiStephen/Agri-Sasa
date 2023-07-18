package com.steve_md.smartmkulima.ui.fragments.main

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.model.AgriTechCompany
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocateAgriTechCompaniesFragment : Fragment() , OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_locate_agri_tech_companies, container, false)
        // Initialize the MapView
        mapView = rootView.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        (activity as AppCompatActivity).supportActionBar?.hide()
        return rootView
    }

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

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
        } else {
            // Request location permission
            requestLocationPermission()
        }

        // Add sample agrovet markers
        val agrovets = getAgrovetsData()
        for (agrovet in agrovets) {
            val location = LatLng(agrovet.latitude, agrovet.longitude)
            googleMap.addMarker(MarkerOptions().position(location).title(agrovet.name))
                ?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
        }

        if (agrovets.isNotEmpty()) {
            val firstLocation = LatLng(agrovets[0].latitude, agrovets[0].longitude)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLocation, 18f))
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun getAgrovetsData(): List<AgriTechCompany> {
        // Replace with your actual data retrieval logic
        // This is just a sample implementation
        return listOf(
            AgriTechCompany("SIKATA AGRITECH FARMERS CHOICE", 0.5929, 34.5429839),
            AgriTechCompany("ROSE AGRITECH COMPANY", 0.5960, 34.543333),
            AgriTechCompany("JOSEMO AGRITECH & DISTRIBUTORS BUNGOMA", 0.565110, 34.5431684),
            AgriTechCompany("OMUSALE AGRITECH & AGROVET", 0.565095, 34.5431600),
            AgriTechCompany("MULTIDUSH AGRITECH & AGROVET SUPPLIES", 0.565100, 34.545406)
        )
    }
}