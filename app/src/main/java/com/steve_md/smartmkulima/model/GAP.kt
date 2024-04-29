package com.steve_md.smartmkulima.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GAP(
    val nameGAP: String,
    val imageGAP: String,
    val gap: List<GAPtask>
) : Parcelable

@Parcelize
data class GAPtask (
    val taskName: String,
    val startDate: String,
    val endDate: String
) : Parcelable
