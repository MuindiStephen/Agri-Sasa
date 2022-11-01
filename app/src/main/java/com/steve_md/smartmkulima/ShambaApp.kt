package com.steve_md.smartmkulima
/*
 Smart
 Mkulima App by Stephen Muindi
 */

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class ShambaApp : Application() {
    //Timber for logging
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

}