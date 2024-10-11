package com.steve_md.smartmkulima.utils.services

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


/**
 * Handling Location runtime permissions in Android | Kotlin.
 */
object LocationPermissionHelper {
    private val BASIC_PERMISSION = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    private const val BASIC_PERMISSION_REQUESTCODE = 0

    fun hasAccessFinePermission(activity: Activity): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestFineLocationPermission(activity: Activity?) {
        ActivityCompat.requestPermissions(activity!!, BASIC_PERMISSION, BASIC_PERMISSION_REQUESTCODE)
    }

    fun shouldShowRequestPermissionRationale(activity: Activity): Boolean{
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, BASIC_PERMISSION.get(0))
    }
    fun launchPermissionSettings(activity: Activity) {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        intent.data = Uri.fromParts("package", activity.packageName, null)
        activity.startActivity(intent)
    }

}