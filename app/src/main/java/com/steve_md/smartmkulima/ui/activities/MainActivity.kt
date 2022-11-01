package com.steve_md.smartmkulima.ui.activities

/*
* SmartMkulima by Stephen Muindi
* */

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.steve_md.smartmkulima.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // hiding the toolbar
        supportActionBar?.hide()
    }
}