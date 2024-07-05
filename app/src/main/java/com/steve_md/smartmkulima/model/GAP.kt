package com.steve_md.smartmkulima.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GAP(
    val nameGAP: String,
    val imageGAP: String,
    val gap: List<GAPtask>
): Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as GAP

        if (nameGAP != other.nameGAP) return false
        if (imageGAP != other.imageGAP) return false
        if (gap != other.gap) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nameGAP.hashCode()
        result = 31 * result + nameGAP.hashCode()
       // result = 31 * result + gap.hashCode()
        return result
    }
}

/**
 *
 * if (Parcelable::class.java.isAssignableFrom(GAP::class.java)) {
 *           result.putParcelable("gap", this.gap as Parcelable)
 *         } else if (Serializable::class.java.isAssignableFrom(GAP::class.java)) {
 *           result.putSerializable("gap", this.gap as Serializable)
 *         } el
 */

@Parcelize
data class GAPtask (
    val taskName: String,
    val startDate: String,
    val endDate: String
) : Parcelable
