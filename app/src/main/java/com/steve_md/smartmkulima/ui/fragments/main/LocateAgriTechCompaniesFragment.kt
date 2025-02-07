package com.steve_md.smartmkulima.ui.fragments.main

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.AgrodealerAdapter
import com.steve_md.smartmkulima.model.AgroDealer
import com.steve_md.smartmkulima.model.AgroDealerOffers
import com.steve_md.smartmkulima.ui.fragments.others.LocationProvider
import com.steve_md.smartmkulima.utils.displaySnackBar
import com.steve_md.smartmkulima.utils.isInternetAvailable
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
    private lateinit var locationProvider: LocationProvider

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
                    AgrodealerAdapter.OnClickListener { agrodealer, distance ->
                    Timber.i("Agrodealer: ${agrodealer.name}")

                        val directions = LocateAgriTechCompaniesFragmentDirections.actionLocateAgriTechCompaniesFragmentToViewAgroDealerInDetailFragment(
                            agrodealer, distance
                        )

                        findNavController().navigate(directions)
                })
            }
        }


    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        if (!isInternetAvailable(requireContext())) {
            showInternetErrorDialog()
            return
        } else {
            displaySnackBar("Agrodealers location updated successfully")
        }

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

        if (!isLocationEnabled()) {
            promptEnableLocationServices()
            return
        }

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

        locationProvider = LocationProvider(this.requireContext())
        locationProvider.getLastKnownLocation { location ->

            if (location == null) {
                displaySnackBar("Unable to get current location. Please check your settings")
                return@getLastKnownLocation
            } else {
                Timber.tag("LocateNearAgroDealersFragment").e("Location services are enabled." +
                        " Showing nearby agro-dealers to the user.")
            }

            val userLatLng = LatLng(location.latitude, location.longitude)
            val agrodealers = getAgroDealersData().filter { agrovet ->
                val agrovetLatLng = LatLng(agrovet.latitude, agrovet.longitude)
                calculateDistance(userLatLng, agrovetLatLng) <= SEARCH_RADIUS_METERS
            }

                // Add filtered Agro-Dealers Markers to the map
                for (agrodealer in agrodealers) {
                    val location2 = LatLng(agrodealer.latitude, agrodealer.longitude)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(location2)
                            .title(agrodealer.name)
                    )?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))

                }

            userLatLng.let { CameraUpdateFactory.newLatLngZoom(it, 18f) }
                .let { googleMap.moveCamera(it)
                    googleMap.animateCamera(it)}

                if (agrodealers.isNotEmpty()) {

                    val boundsBuilder = LatLngBounds.Builder()

                    val userLatLng1 = LatLng(location.latitude, location.longitude)
                    val agrodealers1 = getAgroDealersData().filter { agrovet ->
                        val agrovetLatLng = LatLng(agrovet.latitude, agrovet.longitude)
                        calculateDistance(userLatLng1, agrovetLatLng) <= SEARCH_RADIUS_METERS
                    }

                    // googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 18f))
                    view?.findViewById<TextView>(R.id.textViewAgrodealersNotavailable)?.visibility = View.GONE
                    view?.findViewById<LottieAnimationView>(R.id.LottieNoRecords)?.visibility = View.GONE

                    for (agrodealer in agrodealers1) {
                        val location1 = LatLng(agrodealer.latitude, agrodealer.longitude)
                        googleMap.addMarker(
                            MarkerOptions()
                                .position(location1)
                                .title(agrodealer.name)
                        )?.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                        boundsBuilder.include(location1)
                    }


                    userLatLng1.let { CameraUpdateFactory.newLatLngZoom(it, 18f) }
                        .let { googleMap.moveCamera(it) }

                    // include user's location in bounds
                    userLatLng.let { boundsBuilder.include(it) }

                    /**
                     * Adjust the map's camera zoom level after calculating the bounds that
                     * include all agro-dealers within a 50 km radius.
                     * using the `LatLngBounds` .
                     */
                    val bounds = boundsBuilder.build()
                    val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 100) // 100 is padding
                    googleMap.moveCamera(cameraUpdate)
                    googleMap.animateCamera(cameraUpdate)
                }

                if(agrodealers.isEmpty()) {
                    view?.findViewById<TextView>(R.id.textViewAgrodealersNotavailable)?.visibility = View.VISIBLE
                    view?.findViewById<LottieAnimationView>(R.id.LottieNoRecords)?.visibility = View.VISIBLE
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

    // Prompt user to enable location if it’s turned off
    private fun promptEnableLocationServices() {
        AlertDialog.Builder(requireContext())
            .setTitle("Enable Location Services")
            .setMessage("Location services are required to find nearby agro-dealers. Please enable them.")
            .setPositiveButton("Settings") { _, _ ->
                startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    // Check if location services are enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    // Registering Verified AgroDealers within this platform
    // Gathered & Collected Agro-Dealers data to display Available AgroDealers
    // filter agrodealers based on proximity to the user's exact location and nearby agrodealers
    private fun getAgroDealersData(): List<AgroDealer> {
        return listOf(

            // Near Bung-oma Town
            AgroDealer(1,"SIKATA AGRITECH FARMERS CHOICE","254712907551", "sikataagrifarm@gmail.com",0.5929, 34.5429839,"Fertilizer","Agricultural","true","6 months duration, monthly payment terms","Caxton House, Room 2",getOffersList()),
            AgroDealer(2, "ROSE AGRITECH COMPANY", "0740408989" ,"roseag@gmail.com",0.5960, 34.543333,"Fertilizer","Agricultural","true","1 year duration, monthly payment terms","Township building",getOffersList()),
            AgroDealer(3,"JOSEMO AGRITECH & DISTRIBUTORS BUNGOMA", "0791347689","josemoagrodealers@yahoo.com",0.565110, 34.5431684,"Fertilizer","Agricultural","true","3 months duration, weekly payment terms","CBD 1st building",getOffersList()),
            AgroDealer(4,"OMUSALE AGRITECH & AGROVET","0747909084","info@omusale.com", 0.565095, 34.5431600,"Fertilizer","Agricultural","true","1 months duration, weekly payment terms","Opp-national bank",getOffersList()),
            AgroDealer(5,"MULTIDUSH AGRITECH & AGROVET SUPPLIES", "","info@multidushagritechsupplies.co.ke", 0.565100, 34.545406,"Fertilizer","Agricultural","true","2 months duration, monthly payment terms","KFA building",getOffersList()),

            // Near Nairobi
            AgroDealer(6,"Farmers Solution Agrovet","254700932932", "farmerssolutionagrovet@gmail.com",-1.2860464,36.8026465,"Agrochemicals","Agricultural inputs","false","Leasing is not available","Business Center, Slip road/Kijabe",getOffersList()),
            AgroDealer(7,"Mifugo Agrovet centre","254701898905", "info@mifugoagrovetcentre.co.ke",-1.286548536,36.8067588,"Fertilizers, farm machinery","Agricultural","true","6 months duration, monthly payment terms","Haile Selassie Ave, next JTM building",getOffersList()),
            AgroDealer(8,"Lessos Agrovets","0712046859", "info@lessonsagrovets.com",-1.2720571,36.7961583,"Fertilizers, farm machinery","Agricultural","true","1 month duration, weekly payment terms","Kapsabet Street, Baraton Building",getOffersList()),
            AgroDealer(9,"DIHA AGRO DEALERS","254745003225", "info@dihaagridealers.com",-0.6799296,36.7066032,"Fertilizers, farm machinery","Agricultural","true","1 month duration, weekly payment terms","Mururi, Kwa John store",getOffersList()),
            AgroDealer(10,"Jumbo Agrovet Limited","254722510291", "info@jumboagrovetltd.com",-1.2885866,36.2516181,"Agrochemicals","Agricultural","true","1 year, 6 months payment terms","Lotus House, Hailes Selassie, Nairobi",getOffersList()),



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
            AgroDealer(11,"Farmsquare Agro Dealers","254712505038", "farmsquareagrodealers@gmail.com",-1.2262432,37.1600463,"Fertilizers, farm machinery","Agricultural","true","2 months duration, monthly payment terms","Kamba Building",getOffersList()),
            // Near Naivasha

            // Near Nakuru

            // Near Eldoret

            // Near Mombasa

            // Near Bomet
        )
    }

    private fun getOffersList() = mutableListOf<AgroDealerOffers>().apply {
        add(
            AgroDealerOffers(
             1, R.drawable.ic_easygro_calcium,
            "EasyGro Calcium fertilizer",
            2500.0,
            1750.0,
            "30% off"
            )
        )
        add(
            AgroDealerOffers(
            2, R.drawable.nimbecidine,
            "Nimbecidine bio-insecticide",
            1500.0,
            1050.0,
            "30% off")
        )
    }


    @Deprecated("Deprecated in Java")
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

        //mapView.onDestroy()

        googleMap.setOnMapClickListener(null)
        googleMap.setOnMarkerClickListener(null)
    }
//    override fun onDestroy() {
//        super.onDestroy()
//        mapView.onDestroy()
//        googleMap.clear()
//        googleMap.setOnMarkerClickListener(null)
//        googleMap.setOnMapClickListener(null)
//    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    private fun showInternetErrorDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("No Internet Connection to load map")
            .setMessage("Please check your internet connection and try again.")
            .setPositiveButton("Retry") { dialog, _ ->
                dialog.dismiss()
                // Retry the map load or any other action
                mapView.getMapAsync(this@LocateAgriTechCompaniesFragment)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

}