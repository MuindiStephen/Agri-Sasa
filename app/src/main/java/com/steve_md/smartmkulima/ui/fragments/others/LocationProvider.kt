package com.steve_md.smartmkulima.ui.fragments.others

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.steve_md.smartmkulima.ui.fragments.main.LocateAgriTechCompaniesFragment
import com.steve_md.smartmkulima.ui.fragments.main.MonitorFarmConditionFragment


/**
 *  Handle the location-related functionality.
 *  This class will be responsible for retrieving the user's location using GPS
 */
class LocationProvider(private val context:Context) {

    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    fun getLastKnownLocation(callback: (Location?) -> Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            //promptUserRequestingPermissions()
           // MonitorFarmConditionFragment().requestLocationPermission()
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            callback(location)
        }
    }

    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }

    private fun promptUserRequestingPermissions() {
        ActivityCompat.requestPermissions(
            Activity().parent,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_REQUEST_CODE
        )
    }
}
