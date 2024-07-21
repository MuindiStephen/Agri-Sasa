package com.steve_md.smartmkulima.utils

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateFormat {

    fun formatDate(timestamp: Long): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-d", Locale.getDefault())
        val date = Date(timestamp)
        return dateFormat.format(date)
    }

    fun formatTime(timestamp: Long): String {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val date = Date(timestamp)
        return timeFormat.format(date)
    }

    @SuppressLint("SimpleDateFormat")
    fun getLastLoginDayAndDate(): String? {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("EEE, MMM d, yyyy, hh:mma")
        return sdf.format(cal.time)
    }

    @SuppressLint("SimpleDateFormat")
    fun getWhenStarts(): String? {
        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("d-MM-yyyy")
        return sdf.format(cal.time)
    }

}