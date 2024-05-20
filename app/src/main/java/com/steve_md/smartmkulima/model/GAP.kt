package com.steve_md.smartmkulima.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GAP(
    val nameGAP: String,
    val imageGAP: String,
    val gap: List<GAPtask> = listOf()
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GAP

        if (nameGAP != other.nameGAP) return false
        // Compare other properties here if necessary

        return true
    }

    override fun hashCode(): Int {
        var result = nameGAP.hashCode()
        result = 31 * result + nameGAP.hashCode()
        // Combine other properties into hash code if necessary
        return result
    }
}

@Parcelize
data class GAPtask (
    val taskName: String,
    val startDate: String,
    val endDate: String
) : Parcelable
