package com.steve_md.smartmkulima.utils

import android.annotation.SuppressLint
import android.view.View
import com.google.android.material.snackbar.Snackbar

object SnackbarHelper {

    private var rootView: View? = null

    fun setRootView(view: View) {
        rootView = view
    }

    fun show(message: String) {
        rootView?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }
}