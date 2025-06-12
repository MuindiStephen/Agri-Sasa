package com.steve_md.smartmkulima.payment.mpesa.dto


import com.google.gson.annotations.SerializedName


data class StkPushSuccessResponse(
    @SerializedName("Body")
    val body: Body
)