package com.steve_md.smartmkulima.ui.fragments.main

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.PolygonOptions
import com.google.maps.android.SphericalUtil
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.ui.fragments.others.LocationProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

/**
 *  Map out farm boundaries
 *  There is automated mapping by placing pins on the map
 *  and manual mapping by walking on the farm for it to detect farm boundaries
 *
 *  App tracks the user's movement and places a path of pins automatically as they walk around their farm,
 *  forming a boundary once the mapping is stopped.
 *  This is achieved by continuously recording GPS points (LatLng) while the user is walking.
 */
@AndroidEntryPoint
class MappingFarmLocationWithPinsFragment : Fragment() ,OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var locationProvider: LocationProvider

    // List of pins to be placed to create a polygon
    private val boundaryPoints = mutableListOf<LatLng>()

    // The polygon which will calculate farm size in hectares
    private var farmPolygon: Polygon? = null
    private var isMappingActive: Boolean = false
    private lateinit var btnStartMapping: Button
    private lateinit var btnStopMapping: Button
    private lateinit var btnRedoMapping: Button
    private lateinit var btnSaveMappedArea: Button

    companion object {
        private const val CURRENT_LOCATION_PERMISSION_REQUEST_CODE: Int = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(
            R.layout.fragment_mapping_farm_location_wih_pins,
            container,
            false
        )

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
       // outState.putString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the map fragment inside this@MappingFarmLocationWithPinsFragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapViewSatellite)
                as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initializing views
        btnStartMapping = view.findViewById(R.id.buttonStartMapping)
        btnStopMapping = view.findViewById(R.id.buttonStopMapping)
        btnRedoMapping = view.findViewById(R.id.buttonRedoMapping)
        btnSaveMappedArea = view.findViewById(R.id.buttonSaveMappedArea)

        btnStartMapping.setOnClickListener {
            isMappingActive = true
            btnStartMapping.visibility = View.GONE
            btnStopMapping.visibility = View.VISIBLE
            showMappingInProgressDialog()
        }

        btnStopMapping.setOnClickListener {
            isMappingActive = false
            btnStopMapping.visibility = View.GONE
            btnRedoMapping.visibility = View.VISIBLE
            btnSaveMappedArea.visibility = View.VISIBLE
        }

        btnRedoMapping.setOnClickListener {
            isMappingActive = false
            resetMapping()
            btnRedoMapping.visibility = View.GONE
            btnSaveMappedArea.visibility = View.GONE
            btnStartMapping.visibility = View.VISIBLE
        }

        btnSaveMappedArea.setOnClickListener {
            // Implement Logic to save the area and coordinates
            saveMappedArea()
        }
    }
    private fun saveMappedArea() {

        // Calculate the farm size (area for the polygon)
        val calculatedFarmSize = SphericalUtil.computeArea(boundaryPoints) // area in Metres squared
        val areaInHectares = calculatedFarmSize / 10000 // area in hectares

        val bdPointString = ArrayList(boundaryPoints)
        // pass / navigate with this data as argument
        val bundle = bundleOf(
            Pair("FARM_SIZE","$areaInHectares"), Pair("FARM_COORDINATES","$bdPointString")
        )

        // Continue to AddNewFarmFieldFragment
        findNavController().navigate(R.id.addNewFarmFieldFragment, bundle)
        findNavController().popBackStack()

    }

    private fun showMappingInProgressDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Mapping In Progress")
            .setMessage("You can now drop pins on the map to mark the boundaries of your farm.")
            .setPositiveButton("I understand", null)
            .show()
    }

    private fun showMappingNotYetStartedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Mapping Has Not Started")
            .setMessage("Simply click the 'Start Mapping' button to begin mapping your farm boundaries.")
            .setPositiveButton("I Understand", null)
            .show()
    }

    private fun resetMapping() {
        farmPolygon?.remove()
        boundaryPoints.clear()
        map.clear()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.mapType = GoogleMap.MAP_TYPE_SATELLITE


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
            getMyCurrentLocation()
        } else {
            // Otherwise, Request location permission by prompting the user returning to the app
            requestLocationPermission()
        }

    }

    private fun getMyCurrentLocation() {
        locationProvider = LocationProvider(this.requireContext())
        locationProvider.getLastKnownLocation { currentUserLocation ->
            val userLocationLatLng = currentUserLocation?.let { location ->
                LatLng(
                    location.latitude,
                    location.longitude
                )
            }

            userLocationLatLng?.let { CameraUpdateFactory.newLatLngZoom(it,15f) }
                ?.let { map.moveCamera(it) }

            // Then click on the map and start mapping

            // Add Logic to handle different cases of farm mapping process and farm coordinates
            map.setOnMapClickListener { latLng ->

                if (isMappingActive) {
                    boundaryPoints.add(latLng)
                    map.addMarker(MarkerOptions().position(latLng))
                    updateFarmPolygon()
                } else {
                    showMappingNotYetStartedDialog()
                }
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateFarmPolygon() {
        farmPolygon?.remove()

        if (boundaryPoints.size >= 3) {
            val polygonOptions = PolygonOptions()
                .addAll(boundaryPoints)
                .strokeWidth(2f)
                .strokeColor(0xFF00FF00.toInt())
                .fillColor(0x5500FF00)

            farmPolygon = map.addPolygon(polygonOptions)

            // Calculate the area in square meters and convert to hectares
            val areaInSquareMeters = SphericalUtil.computeArea(boundaryPoints)
            val areaInHectares = areaInSquareMeters / 10000

            // Display the area
            btnSaveMappedArea.text = "Save Mapped Area: %.2f ha".format(areaInHectares)
            Timber.tag("MapFarmWithPins").e("Save Mapped Area: %.2f ha".format(areaInHectares))
        } else {
            AlertDialog.Builder(requireContext())
                .setTitle("Insufficient Path Points")
                .setMessage("Please mark at least three points to create a farm boundary.")
                .setPositiveButton("OK", null)
                .show()
            btnSaveMappedArea.text = "Mapped Area: 0.0 ha"
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            CURRENT_LOCATION_PERMISSION_REQUEST_CODE
        )
    }
}