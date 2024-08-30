package com.steve_md.smartmkulima.ui.fragments.main

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
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
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.SphericalUtil
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.databinding.FragmentManualWalkingFarmMappingBinding
import com.steve_md.smartmkulima.ui.fragments.others.LocationProvider
import dagger.hilt.android.AndroidEntryPoint

/**
 * Aspect of Manual Mapping by walking in the farm
 */
@AndroidEntryPoint
class ManualWalkingFarmMappingFragment : Fragment() ,OnMapReadyCallback{
    private lateinit var binding: FragmentManualWalkingFarmMappingBinding


    private lateinit var googleMap: GoogleMap
    private lateinit var locationProvider: LocationProvider
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    // List of pins to be placed to create a polygon
    private val pathPoints = mutableListOf<LatLng>()
    private var isMappingActive: Boolean = false
    private var farmPolygon: Polygon? = null


    companion object {
        private const val LOCATION_PERMISSION_CODE: Int = 1
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

        binding.apply {
            buttonStartMapping.setOnClickListener {
                isMappingActive = true
                buttonStartMapping.visibility = View.GONE
                buttonStopMapping.visibility = View.VISIBLE
                showMappingInProgressDialog()
            }

            buttonStopMapping.setOnClickListener {
                isMappingActive = false
                buttonStopMapping.visibility = View.GONE
                buttonRedoMapping.visibility = View.VISIBLE
                buttonSaveMappedArea.visibility = View.VISIBLE
            }

            buttonRedoMapping.setOnClickListener {
                isMappingActive = false
                resetMapping()
                buttonRedoMapping.visibility = View.GONE
                buttonSaveMappedArea.visibility = View.GONE
                buttonStopMapping.visibility = View.VISIBLE
            }

            buttonSaveMappedArea.setOnClickListener {
                saveMapppedArea()
                createPolygon()
            }
        }
    }

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


        // Get current location of the user.
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

    private fun getMyCurrentLocationANDStartManualMapping() {

        locationProvider = LocationProvider(this.requireContext())

        if (isMappingActive) {

            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }

            val locationRequest = LocationRequest.create().apply {
                interval = 5000
                fastestInterval = 2000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }


            // GPS to record user movement , track and provide location updates
            fusedLocationClient.requestLocationUpdates(locationRequest, object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    for (location in locationResult.locations) {
                        val latLng = LatLng(location.latitude, location.longitude)
                        pathPoints.add(latLng)
                        googleMap.addPolyline(PolylineOptions().addAll(pathPoints))
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18f))
                    }
                }
            }, Looper.getMainLooper())


        } else {
            showMappingNotYetStartedDialog()
        }
    }

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

    private fun resetMapping() {
        farmPolygon?.remove()
        pathPoints.clear()
        googleMap.clear()
    }


    private fun createPolygon() {
        if (pathPoints.size >= 3) {
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
            binding.buttonSaveMappedArea.text = "Save Mapped Area: %.2f ha".format(areaInHectares)

        } else {
            // Show a message if the user hasn't marked enough points to create a polygon
            AlertDialog.Builder(requireContext())
                .setTitle("Insufficient Path Points")
                .setMessage("Please mark at least three points to create a farm boundary.")
                .setPositiveButton("OK", null)
                .show()
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
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("Permission Denied")
                    .setMessage("Location permission is required to map your farm boundaries.")
                    .setPositiveButton("OK", null)
                    .show()
            }
        }
    }


}