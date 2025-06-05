package com.steve_md.smartmkulima.payment.mpesa.dto

import com.google.gson.annotations.SerializedName

/**
 * This is the first / initial STK Push Response
 * Since this is what Safaricom returns immediately after initiating the STK push
 */
data class STKPushInitialResponse(
    @SerializedName("MerchantRequestID") val merchantRequestID: String,
    @SerializedName("CheckoutRequestID") val checkoutRequestID: String,
    @SerializedName("ResponseCode") val responseCode: String,
    @SerializedName("ResponseDescription") val responseDescription: String,
    @SerializedName("CustomerMessage") val customerMessage: String
)

// Sample response
/*
{
    "MerchantRequestID":"b18f-4a07-9aff-d6e1fb096b51472694",
    "CheckoutRequestID":"ws_CO_05062025222045312740495903",
    "ResponseCode": "0",
    "ResponseDescription":"Success. Request accepted for processing",
    "CustomerMessage":"Success. Request accepted for processing"
}
 */
