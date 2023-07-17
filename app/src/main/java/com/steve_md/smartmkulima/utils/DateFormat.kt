package com.steve_md.smartmkulima.utils

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
}