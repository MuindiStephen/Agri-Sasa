package com.steve_md.smartmkulima.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * This data model class accommodates both crop cycle and service cycle
 */

@Parcelize
data class Cycle(
    val farmId: String,
    val cropName: String,
    val startDate: String,
    val type: String, // "crop cycle" or "service cycle/livestock"
    val tasks: List<Tasks>
) : Parcelable


@Parcelize
data class Tasks (
    val taskName: String,
    val startDate: String,
    val endDate: String
) : Parcelable
