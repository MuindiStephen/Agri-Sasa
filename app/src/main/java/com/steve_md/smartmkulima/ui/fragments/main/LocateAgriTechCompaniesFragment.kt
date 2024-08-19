package com.steve_md.smartmkulima.ui.fragments.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.AgrodealerAdapter
import com.steve_md.smartmkulima.model.AgroDealer
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


/**
 * AGRO-DEALERS_LOCATION_FRAGMENT
 *
 * Display near Agro-Dealers companies :)
 *
 * AgroDealers Location
 * -  AgroDealer's name, Contact details, Location (0.95 km away),
 * -  Farm inputs, Leasing options, Services offering
 *
 * - Filter Agro-Dealers with proximity to the user's location
 */
@AndroidEntryPoint
class LocateAgriTechCompaniesFragment : Fragment() , OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val SEARCH_RADIUS_METERS = 50000 // 50 km radius
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(
            R.layout.fragment_locate_agri_tech_companies,
            container,
            false
        )
        // Initialize the MapView
        mapView = rootView.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)

        // Trigger map ready to be used, from the callback (OnMapReadyCallback)
        mapView.getMapAsync(this)

        (activity as AppCompatActivity).supportActionBar?.hide()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpBinding()
    }

    private fun setUpBinding() {
        view?.findViewById<LinearLayout>(R.id.topbar_linear_layout)?.setOnClickListener {
           // findNavController().popBackStack(R.id.newFarmingTechnologyFragment, true)
           findNavController().navigateUp()
        }


        // Inflate Data To Recycler View
        val recyclerView = view?.findViewById<RecyclerView>(R.id.agrodealersListRecView)
        recyclerView!!.layoutManager = LinearLayoutManager(requireContext())

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationPermission()
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val userLatLng = LatLng(it.latitude, it.longitude)
                val filteredAgrodealers = getAgroDealersData().filter { agrovet ->

                    val agroDealerLatLng = LatLng(agrovet.latitude,agrovet.longitude)
                    calculateDistance(userLatLng, agroDealerLatLng) <= SEARCH_RADIUS_METERS
                }

                recyclerView.adapter = AgrodealerAdapter(
                    filteredAgrodealers,
                    userLatLng,
                    AgrodealerAdapter.OnClickListener { agrodealer ->
                    Timber.i("Agrodealer: ${agrodealer.name}")

                        val directions = LocateAgriTechCompaniesFragmentDirections.actionLocateAgriTechCompaniesFragmentToViewAgroDealerInDetailFragment(
                            agrodealer
                        )

                        findNavController().navigate(directions)

                })
            }
        }


    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        /**
         * If permissions have been already granted
         */
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
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
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            promptUserForLocationPermissions()
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                val userLatLng = LatLng(it.latitude, it.longitude)
                val agrodealers = getAgroDealersData().filter { agrovet ->
                    val agrovetLatLng = LatLng(agrovet.latitude, agrovet.longitude)
                    calculateDistance(userLatLng, agrovetLatLng) <= SEARCH_RADIUS_METERS
                }

                // Add filtered Agro-Dealers Markers to the map
                for (agrodealer in agrodealers) {
                    val location = LatLng(agrodealer.latitude, agrodealer.longitude)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(location)
                            .title(agrodealer.name)
                    )?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                }

                if (agrodealers.isNotEmpty()) {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 13f))
                }
            }
        }
    }


    // Nearby within 50 kilometres
    private fun calculateDistance(startLatLng: LatLng, endLatLng: LatLng): Float {
        val results = FloatArray(1)
        Location.distanceBetween(
            startLatLng.latitude, startLatLng.longitude,
            endLatLng.latitude, endLatLng.longitude,
            results
        )
        return results[0]
    }


    /**
     * Prompt the user to enter permissions In case permissions aren't granted / == null
     */
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    // Gathered & Collected Agro-Dealers data to display Available AgroDealers
    // filter agrodealers based on proximity to the user's exact location and nearby agrodealers
    private fun getAgroDealersData(): List<AgroDealer> {
        return listOf(

            // Near Bung-oma Town
            AgroDealer("SIKATA AGRITECH FARMERS CHOICE","254712907551", "sikataagrifarm@gmail.com",0.5929, 34.5429839,"Fertilizer","Agricultural","true","6 months duration, monthly payment terms"),
            AgroDealer("ROSE AGRITECH COMPANY", "0740408989" ,"roseag@gmail.com",0.5960, 34.543333,"Fertilizer","Agricultural","true","1 year duration, monthly payment terms"),
            AgroDealer("JOSEMO AGRITECH & DISTRIBUTORS BUNGOMA", "0791347689","josemoagrodealers@yahoo.com",0.565110, 34.5431684,"Fertilizer","Agricultural","true","3 months duration, weekly payment terms"),
            AgroDealer("OMUSALE AGRITECH & AGROVET","0747909084","info@omusale.com", 0.565095, 34.5431600,"Fertilizer","Agricultural","true","1 months duration, weekly payment terms"),
            AgroDealer("MULTIDUSH AGRITECH & AGROVET SUPPLIES", "","info@multidushagritechsupplies.co.ke", 0.565100, 34.545406,"Fertilizer","Agricultural","true","2 months duration, monthly payment terms"),

            // Near Nairobi
            AgroDealer("Farmers Solution Agrovet","254700932932", "farmerssolutionagrovet@gmail.com",-1.2860464,36.8026465,"Agrochemicals","Agricultural inputs","false","Leasing is not available"),
            AgroDealer("Mifugo Agrovet centre","254701898905", "info@mifugoagrovetcentre.co.ke",-1.286548536,36.8067588,"Fertilizers, farm machinery","Agricultural","true","6 months duration, monthly payment terms"),


            // Near Kiambu

            // Near Makueni

            // Near Kijabe

            // Near Soko Mjinga

            // Near Murang'a

            // Near Nyandarua

            // Near Uthiru

            // Near Thika

            // Near Kitui

            // Near Machakos

            // Near Naivasha

            // Near Nakuru

            // Near Eldoret

            // Near Mombasa

            // Near Bomet
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                // Enable the location
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    promptUserForLocationPermissions()
                    return
                }
                googleMap.isMyLocationEnabled = true
            }
        } else {
            getMyCurrentLocation()
        }
    }

    private fun promptUserForLocationPermissions() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().isDestroyed

        googleMap.clear()

        mapView.onDestroy()

        googleMap.setOnMapClickListener(null)
        googleMap.setOnMarkerClickListener(null)
    }
    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
        googleMap.clear()
        googleMap.setOnMarkerClickListener(null)
        googleMap.setOnMapClickListener(null)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}