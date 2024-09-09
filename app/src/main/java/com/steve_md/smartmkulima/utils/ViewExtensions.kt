package com.steve_md.smartmkulima.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.steve_md.smartmkulima.R

//Extension load an image into an imageview
fun ImageView.load(resource: Any) {
    when (resource) {
        is String, is Int, is Drawable -> {
            Glide.with(this.context)
                .load(resource)
                .into(this)
        }
        else -> {
            throw IllegalArgumentException("Cannot load resource")
        }
    }
}

//Toast a message
fun Context.toast(string: String) {
    Toast.makeText(this, string, Toast.LENGTH_SHORT).show()
}

fun View.crossFade(view2: View) {
    this.apply {
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        alpha = 0f
        visibility = View.VISIBLE

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        animate()
            .alpha(1f)
            .setDuration(300.toLong())
            .setListener(null)
    }
    // Animate the loading view to 0% opacity. After the animation ends,
    // set its visibility to GONE as an optimization step (it won't
    // participate in layout passes, etc.)
    view2.animate()
        .alpha(0f)
        .setDuration(300.toLong())
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                view2.visibility = View.GONE
            }
        })
}

// Gets initial letters of character strings
// eg. Agri Sasa -> initials will be (AS)
fun String.getInitials(): String {
    val array = this.split(" ")
    return if (array.size == 1) {
        array[0].substring(0, 1)
    } else {
        array[0].substring(0, 1) + array[1].substring(0, 1)
    }
}

fun AutoCompleteTextView.setUpSpinner(arrayResource: Int, onItemClick : (parent: AdapterView<*>?,
                                                                         view: View?,
                                                                         position: Int,
                                                                         id: Long)  -> Unit) {
    val providers = resources.getStringArray(arrayResource)
    val providerAdapter = ArrayAdapter(
        context,
        R.layout.spinner_list_item_merchant,
        providers
    )
    setAdapter(providerAdapter)
    setOnFocusChangeListener { v, hasFocus ->
    }
    setOnItemClickListener { parent, view, position, id ->
        onItemClick(parent,view,position,id )
    }
}