package com.steve_md.smartmkulima

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber


@HiltAndroidApp
class ShambaApp : Application(){
    override fun onCreate() {
        super.onCreate()
        timber()
    }
    private fun timber() {
        Timber.plant(Timber.DebugTree())
    }
}