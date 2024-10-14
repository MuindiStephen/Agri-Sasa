package com.steve_md.smartmkulima

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/**
 * Serves as the main entry point of this android app
 * With dependency injection (dagger-hilt)
 */
@HiltAndroidApp
class ShambaApp : Application() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    override fun onCreate() {
        super.onCreate()
        timber()
       // setNetworkSecurity()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("ui_mode", Context.MODE_PRIVATE)
        val itemUIMode: Boolean = sharedPreferences.getBoolean("ISCHECKED", false)
        Timber.d("UI Theme: $itemUIMode")
        uiMode(itemUIMode)

        initializeSDKS()
    }

    private fun initializeSDKS() {
        val executorService: ExecutorService = Executors.newFixedThreadPool(2)
        executorService.execute {
            initializeAnalyticsSDKS(context = this.applicationContext)
        }

        executorService.execute {
            initializeCrashReportingSDKS()
        }
    }

    private fun initializeCrashReportingSDKS() {

    }

    private fun initializeAnalyticsSDKS(context: Context) {
        // Initialize Firebase Analytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(context)

        // Example of logging an event after initialization
        val bundle = Bundle().apply {
            putString(FirebaseAnalytics.Param.METHOD, "app_start")
        }

        /*
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param(FirebaseAnalytics.Param.ITEM_ID, bundle);
            param(FirebaseAnalytics.Param.ITEM_NAME, name);
            param(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        }
         */

       firebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle)
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

    /**
     * StrictMode is a developer tool which detects things you might be doing
     * by accident and brings them to your attention so you can fix them
     *
     */
    private fun setNetworkSecurity() {
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder
            .detectAll()
            .detectLeakedSqlLiteObjects()
            .detectLeakedClosableObjects()
            .penaltyLog()
            .penaltyDeath()
            .build())
    }

    private fun timber() {
        Timber.plant(Timber.DebugTree())
    }


}