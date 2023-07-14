package com.steve_md.smartmkulima.data.dto


import com.google.gson.annotations.SerializedName

data class EquipmentsResponseItem(
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
)