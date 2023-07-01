package com.steve_md.smartmkulima.payment.mpesa.dto


import com.google.gson.annotations.SerializedName

data class CallbackMetadata(
    @SerializedName("Item")
    val item: List<Item>
)