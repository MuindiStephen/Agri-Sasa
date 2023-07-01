package com.steve_md.smartmkulima.payment.mpesa.dto


import com.google.gson.annotations.SerializedName

data class StkCallback(
    @SerializedName("CallbackMetadata")
    val callbackMetadata: CallbackMetadata,
    @SerializedName("CheckoutRequestID")
    val checkoutRequestID: String,
    @SerializedName("MerchantRequestID")
    val merchantRequestID: String,
    @SerializedName("ResultCode")
    val resultCode: Int,
    @SerializedName("ResultDesc")
    val resultDesc: String
)