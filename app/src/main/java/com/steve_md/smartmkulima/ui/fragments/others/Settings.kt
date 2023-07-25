package com.steve_md.smartmkulima.ui.fragments.others

import android.os.Bundle
import android.preference.PreferenceFragment
import com.steve_md.smartmkulima.R

class Settings: PreferenceFragment() {
    @Deprecated("Deprecated in Java")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.preferences)
    }
}