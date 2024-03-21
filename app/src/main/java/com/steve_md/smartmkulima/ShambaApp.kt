package com.steve_md.smartmkulima

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
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

    /**
     * Support for Dark and Light mode
     */
    private fun uiMode(itemUIMode: Boolean) {
        if (itemUIMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        }
        else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
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