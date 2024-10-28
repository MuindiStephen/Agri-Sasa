package com.steve_md.smartmkulima.utils

import android.content.Context
import android.content.pm.PackageManager
import timber.log.Timber


/**
 * Call this extension function if to find your app version (PFS)
 */
fun getAppVersionName(context: Context): String? {
    try {
        val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        return packageInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        Timber.tag("VERSION_NAME_ERROR").i(e.message ?: "Version Error Message is Null")
    }
    return null
}