package com.steve_md.smartmkulima.ui.fragments.main

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.SphericalUtil
import com.google.maps.android.SphericalUtil.computeOffsetOrigin
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentManualWalkingFarmMappingBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 * Aspect of Manual Mapping by walking in the farm
 */
@AndroidEntryPoint
class ManualWalkingFarmMappingFragment : Fragment() ,OnMapReadyCallback {

    private lateinit var binding: FragmentManualWalkingFarmMappingBinding

    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // List of Pins to be Placed to Create a Polygon
    private val pathPoints = mutableListOf<LatLng>()
    private var isMappingActive: Boolean = false
    private var farmPolygon: Polygon? = null

    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var locationCallback: LocationCallback


    companion object {
        private const val LOCATION_PERMISSION_CODE: Int = 1000
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentManualWalkingFarmMappingBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapViewSatellite)
                as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                if (isMappingActive) {
                    for (location in locationResult.locations) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        pathPoints.add(latLng)
                        googleMap.addMarker(MarkerOptions().position(latLng))

                        // Draw a polyline connecting the pathPoints
                        googleMap.addPolyline(
                            PolylineOptions()
                                .addAll(pathPoints)
                                .color(0xFF0000FF.toInt())  // Blue color
                                .width(5f)
                        )
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))

                        updatePolygonArea()
                    }
                }
            }
        }

         //set up location manager

        binding.apply {
            buttonStartMapping.setOnClickListener {

                val locationRequest = LocationRequest.create().apply {
                    interval = 5000
                    fastestInterval = 2000
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }

                isMappingActive = true

                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestLocationPermission()
                }
                fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
                buttonStartMapping.visibility = View.GONE
                buttonStopMapping.visibility = View.VISIBLE
                showMappingInProgressDialog()
            }

            buttonStopMapping.setOnClickListener {
                isMappingActive = false
                fusedLocationClient.removeLocationUpdates(locationCallback) //
                buttonStopMapping.visibility = View.GONE
                buttonRedoMapping.visibility = View.VISIBLE
                buttonSaveMappedArea.visibility = View.VISIBLE

                // Draw the polygon to show the area the farmer has walked around
                createPolygon()
            }

            buttonRedoMapping.setOnClickListener {
                isMappingActive = false
                resetMapping()
                buttonRedoMapping.visibility = View.GONE
                buttonSaveMappedArea.visibility = View.GONE
                buttonStopMapping.visibility = View.VISIBLE
            }

            buttonSaveMappedArea.setOnClickListener {

                // create polygon and calculate the area in the ploygon
                createPolygon()

                // then navigate
                saveMapppedArea()
            }
        }
    }


    /*
    private fun getPolygonCorners(startLatLng: LatLng, endLng: LatLng) {
        val corners = arrayOfNulls<LatLng>(4)
        corners[0] = computeOffsetOrigin(endLng, 12.0, 16.0) // test dummy values
    }
     */


    private fun saveMapppedArea() {

        val calculatedFarmSize = SphericalUtil.computeArea(pathPoints) // area in Metres squared
        val areaInHectares = calculatedFarmSize / 10000 // area in hectares

        val bdPointString = ArrayList(pathPoints)
        // pass / navigate with this data as argument
        val bundle = bundleOf(
            Pair("FARM_SIZE","$areaInHectares"), Pair("FARM_COORDINATES","$bdPointString")
        )
        findNavController().navigate(R.id.addNewFarmFieldFragment, bundle)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        this.googleMap.mapType = GoogleMap.MAP_TYPE_SATELLITE

        // setCriteria()

        // Get current location of the user.
        // as it zooms
        /**
         * If permissions have been already granted
         */
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            googleMap.isMyLocationEnabled = true
            getMyCurrentLocationANDStartManualMapping()
        } else {
            // Otherwise, Request location permission by prompting the user returning to the app
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_CODE
        )
    }

    /**
     * Zoom and give the user's
     * current location on the Map.
     */
    private fun getMyCurrentLocationANDStartManualMapping() {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 18f))
            }
        }

        val polylineOptions = PolylineOptions().color(0xFF0000FF.toInt()).width(5f)

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (isMappingActive) {
                Timber.e("Manual Mapping: MAPPING is ON.")
                val locationRequest = LocationRequest.create().apply {
                    interval = 5000
                    fastestInterval = 2000
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)
                            for (location in locationResult.locations) {
                                val latLng = LatLng(location.latitude, location.longitude)
                                pathPoints.add(latLng)

                                // Update the path with polyline and markers
                                googleMap.addMarker(MarkerOptions().position(latLng))
                                polylineOptions.add(latLng)
                                googleMap.addPolyline(polylineOptions)

                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))

                                updatePolygonArea()
                            }
                        }
                    },
                    Looper.getMainLooper()
                )
            } else {
                showMappingNotYetStartedDialog()
                Timber.e("Manual Mapping: MAPPING FAILED.")
            }
        } else {
            requestLocationPermission()
        }
    }



    /*
    private fun getMyCurrentLocationANDStartManualMapping() {


        // Zoom to current location
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        currentLatLng,
                        18f
                    )
                ) // Zoom to current location
            }
        }

        // Initialize PolylineOptions to track the walking path
        val polylineOptions = PolylineOptions()
            .color(0xFF0000FF.toInt()) // Blue color for the path
            .width(5f)


        if (isMappingActive) {

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestLocationPermission()
            } else {


                /**
                 * Real time tracking
                 */
                val locationRequest = LocationRequest.create().apply {
                    interval = 5000
                    fastestInterval = 2000
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                }


                /**
                 * Geographic Information Systems.
                 */

                // GPS to record user movement , track and provide location updates.
                // Start plotting immediately.
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    object : LocationCallback() {
                        override fun onLocationResult(locationResult: LocationResult) {
                            super.onLocationResult(locationResult)
                            for (location in locationResult.locations) {
                                val latLng = LatLng(location.latitude, location.longitude)
                                pathPoints.add(latLng)

                                googleMap.addMarker(MarkerOptions().position(latLng))

                                polylineOptions.add(latLng)
                                googleMap.addPolyline(polylineOptions)

                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))

                                updatePolygonArea()
                            }
                        }
                    },
                    Looper.getMainLooper()
                )
            }
            } else {
                showMappingNotYetStartedDialog()
            }
        }
*/

    private fun updatePolygonArea() {
        if (pathPoints.size > 2) {
            val areaInSquareMeters = SphericalUtil.computeArea(pathPoints)
            val areaInHectares = areaInSquareMeters / 10000

            // Display the area in real time
            binding.buttonSaveMappedArea.text = "Area Mapped: ${areaInSquareMeters} ha"
                // "Area: %.7f ha".format(areaInHectares)
               // "Area Mapped: ${areaInSquareMeters} ha"
            Timber.d("Updated Polygon => Mappped Area: ${areaInHectares} hectares")

        }
    }


    /**
     * Set map location criteria to follow while setting up
     * location updates
     */

    /*
    private fun setCriteria() {
        // Location criteria
        val locationCriteria : Criteria = Criteria()
        locationCriteria.setAccuracy(Criteria.ACCURACY_FINE)
        locationCriteria.setPowerRequirement(Criteria.POWER_HIGH)
        locationCriteria.setAltitudeRequired(false)
        locationCriteria.setSpeedRequired(false)
        locationCriteria.setCostAllowed(true)
        locationCriteria.setBearingRequired(false)
        locationCriteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH)
        locationCriteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH)
    }

     */

    private fun showMappingNotYetStartedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Mapping Has Not Started")
            .setMessage("Simply click the 'Start Mapping' button to begin mapping your farm boundaries.")
            .setPositiveButton("I Understand", null)
            .show()
    }

    private fun showMappingInProgressDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Mapping In Progress")
            .setMessage("You can now walk around your farm to let the app track your movements, " +
                    "then tap 'Stop Mapping' when you're done.")
            .setPositiveButton("I understand", null)
            .show()
    }


    private fun setupLocationManager() {

        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE)
                as LocationManager


        locationListener = LocationListener { location ->
            // Handle new location
            val latitude = location.latitude
            val longitude = location.longitude

            // Update your UI or send location data to a server
            Timber.d("Current Location: $latitude || $longitude")
        }

        // Check for the appropriate location provider and request updates
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                5000L, // Time in milliseconds between location updates
                10f,   // Distance in meters between location updates
                locationListener
            )

            // permissions granted proceed with location updates.
            setupLocationManager()

        } else {
            requestLocationPermission()
            setupLocationManager()
        }
    }

    private fun resetMapping() {
        farmPolygon?.remove()
        pathPoints.clear()
        googleMap.clear()
    }

    private fun createPolygon() {
        if (pathPoints.size > 0) {
            farmPolygon = googleMap.addPolygon(
                PolygonOptions()
                    .addAll(pathPoints)
                    .fillColor(0x330000FF)  // Semi-transparent blue fill color
                    .strokeColor(0xFF0000FF.toInt())  // Blue stroke color
                    .strokeWidth(5f)  // Width of the stroke
            )

            // Calculate the area in square meters and convert to hectares
            val areaInSquareMeters = SphericalUtil.computeArea(pathPoints)
            val areaInHectares = areaInSquareMeters / 10000

            // Display the area
            binding.buttonSaveMappedArea.text =  "Save Mapped Area: $areaInSquareMeters"
                //"Save Mapped Area: %.7f ha".format(areaInHectares)
            Timber.d("Mapped $areaInHectares ha")

        }
        else {
            binding.buttonSaveMappedArea.text = "Mapped Area: 0.0 ha"
            Timber.d("Mapped 0.0ha")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getMyCurrentLocationANDStartManualMapping()
                // permissions granted proceed with location updates.
                setupLocationManager()
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Permission Denied")
                    .setMessage("Location permission is required to map your farm boundaries.")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (::locationManager.isInitialized) {
            locationManager.removeUpdates(locationListener)
        }
    }


    /**
     * Lifecycle callbacks.
     */
    // onStop() -> remove location updates
    // onResume() -> map not null, map.clear(), initiate new location updates
    //onPause() -> remove location updates
}