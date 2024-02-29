package com.steve_md.smartmkulima

import android.app.Application
import android.os.Build
import android.os.StrictMode
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class ShambaApp : Application() {
    override fun onCreate() {
        super.onCreate()
        timber()
        setNetworkSecurity()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("ui_mode", Context.MODE_PRIVATE)
        val itemUIMode: Boolean = sharedPreferences.getBoolean("ISCHECKED", false)
        Timber.d("UI Theme: $itemUIMode")
        uiMode(itemUIMode)
    }

    private fun setNetworkSecurity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
        }
    }

    private fun timber() {
        Timber.plant(Timber.DebugTree())
    }
}