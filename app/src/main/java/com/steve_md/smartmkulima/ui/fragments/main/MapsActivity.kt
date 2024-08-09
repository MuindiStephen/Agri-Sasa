package com.steve_md.smartmkulima.ui.fragments.main

import android.Manifest
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.steve_md.smartmkulima.R
import com.steve_md.smartmkulima.adapter.mapadapter.MarkerinfoWindowAdapter
import com.steve_md.smartmkulima.databinding.ActivityMapsBinding
import com.steve_md.smartmkulima.model.mapmodels.Place
import com.steve_md.smartmkulima.model.mapmodels.PlacesReader
import com.steve_md.smartmkulima.utils.BitmapHelper
import com.steve_md.smartmkulima.utils.PlaceRenderer
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var bicycleIcon: BitmapDescriptor


    private val places: List<Place> by lazy {
        PlacesReader(this).read()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        // check app permissions
        reqMultiplePermissions.launch(REQUIREDPERMISSIONS)

        val color = getColor(R.color.colorPrimary)
        bicycleIcon = BitmapHelper.vectorToBitmap(
            this, R.drawable.baseline_warehouse_24, color)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map_fragment) as SupportMapFragment

        mapFragment.getMapAsync(this)

        // add marker from places.json
        mapFragment.getMapAsync { googleMap ->
            // addMarkers(googleMap)
            addClusteredMarkers(googleMap)

            googleMap.uiSettings.isZoomControlsEnabled = true
            googleMap.uiSettings.isZoomGesturesEnabled = true


            // Ensure all places are visible in the map.
            googleMap.setOnMapLoadedCallback {
                val bounds = LatLngBounds.builder()
                places.forEach { bounds.include(it.latLng) }
                googleMap.moveCamera(
                    CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))
            }
        }

    }

    private fun addClusteredMarkers(googleMap: GoogleMap) {
        // Create the ClusterManager class and set the custom renderer.
        val clusterManager = ClusterManager<Place>(this, googleMap)
        clusterManager.renderer =
            PlaceRenderer(
                this,
                googleMap,
                clusterManager
            )

        // Set custom info window adapter
        clusterManager
            .markerCollection
            .setInfoWindowAdapter(MarkerinfoWindowAdapter(this))

        // Add the places to the ClusterManager.
        clusterManager.addItems(places)
        clusterManager.cluster()


//        clusterManager.setOnClusterItemClickListener { item ->
//            addCircle(googleMap, item)
//            return@setOnClusterItemClickListener false
//        }
        // Set ClusterManager as the OnCameraIdleListener so that it
        // can re-cluster when zooming in and out.
        googleMap.setOnCameraIdleListener {
            clusterManager.onCameraIdle()
        }
    }

    private var circle: Circle? = null
    private fun addCircle(googleMap: GoogleMap, item: Place?) {
        circle?.remove()
        circle = googleMap.addCircle(
            CircleOptions()
                .center(item!!.latLng)
                .radius(1000.0)
                .fillColor(
                    ContextCompat.getColor(
                    this, R.color.translucent))
                .strokeColor(ContextCompat.getColor(
                    this, R.color.colorPrimary))
        )
    }

    private fun addMarkers(googleMap: GoogleMap) {
        places.forEach { place ->
            googleMap.addMarker(
                MarkerOptions()
                    .title(place.name)
                    .position(place.latLng)
            )
        }
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0

        // Add a marker in Kenya and move the camera
        // test
        val sf = LatLng(-1.2860732, 36.8103714)
        mMap.addMarker(MarkerOptions()
            .position(sf)
            .title("Marker in Kenya"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sf))
        mMap.animateCamera(CameraUpdateFactory.zoomIn())
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13f), 2000, null)
    }

    private val REQUIREDPERMISSIONS = arrayOf(
        Manifest.permission.INTERNET,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_NETWORK_STATE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    // app permission
    private val reqMultiplePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach {
            Timber.d("mainAct: Permission: ${it.key} = ${it.value}")
            if (!it.value) {
                // toast
                Timber.tag(this@MapsActivity.toString()).i("Permission: ${it.key} denied!", 1)
                finish()
            }
        }
    }
}