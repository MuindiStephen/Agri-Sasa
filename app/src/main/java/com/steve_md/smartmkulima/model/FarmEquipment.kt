package com.steve_md.smartmkulima.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class FarmEquipment(
    @SerializedName("id")
    val id: Int,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("priceHire")
    val priceHire: String,
    @SerializedName("year")
    val year: String
) : Parcelable
