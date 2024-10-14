package com.steve_md.smartmkulima.utils


import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.steve_md.smartmkulima.R

class OverlayService : Service() {

    private lateinit var windowManager: WindowManager
    private lateinit var overlayView: View

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()

        // Inflate your overlay layout
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        overlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // Use TYPE_PHONE for older versions
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )

        // Specify the overlay position on the screen
        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        params.x = 0
        params.y = 100

        // Add the view to the window
        windowManager.addView(overlayView, params)

        // Set up click listeners for the overlay
        overlayView.setOnClickListener {
            // Handle clicks on the overlay
            stopSelf() // This will remove the overlay
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        // Remove the overlay view
        if (::overlayView.isInitialized) {
            windowManager.removeView(overlayView)
        }
    }
}
