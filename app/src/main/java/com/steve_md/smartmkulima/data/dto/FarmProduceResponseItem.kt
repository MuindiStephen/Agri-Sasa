package com.steve_md.smartmkulima.data.dto


import com.google.gson.annotations.SerializedName

data class FarmProduceResponseItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("productImageUrl")
    val productImageUrl: String,
    @SerializedName("productPrice")
    val productPrice: String,
    @SerializedName("productTitle")
    val productTitle: String
)