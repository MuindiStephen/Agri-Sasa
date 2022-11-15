package com.steve_md.smartmkulima.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment


fun Fragment.toast(text:String) {
    Toast.makeText(
        requireContext(),
        text,
        Toast.LENGTH_LONG
    ).show()
}

fun View.hideKeyboard() {
    val closeKeyboard =
        this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    closeKeyboard.hideSoftInputFromWindow(this.windowToken, 0)
}