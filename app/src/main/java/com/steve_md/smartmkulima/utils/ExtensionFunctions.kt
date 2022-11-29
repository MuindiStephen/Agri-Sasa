package com.steve_md.smartmkulima.utils

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar


fun Fragment.toast(text:String) {
    Toast.makeText(
        requireContext(),
        text,
        Toast.LENGTH_LONG
    ).show()
}


fun Fragment.hideKeyboard(): Boolean {
    return (context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
        .hideSoftInputFromWindow((activity?.currentFocus ?: View(context)).windowToken, 0)
}

fun Fragment.snackBar(text: String) {
    Snackbar.make(requireView(), text, Snackbar.LENGTH_INDEFINITE)

        .show()
}

fun Fragment.displaySnackBar(text: String) {
    Snackbar.make(requireView(), text, Snackbar.LENGTH_SHORT)
        .show()
}

