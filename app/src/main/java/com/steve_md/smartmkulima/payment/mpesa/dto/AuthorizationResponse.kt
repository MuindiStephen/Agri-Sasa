package com.steve_md.smartmkulima.payment.mpesa.dto

import com.google.gson.annotations.SerializedName

/**
 * @param [accessToken] - The exact token
 * @param [expiresIn] - When the token expires
 *
 *
 * */

data class AuthorizationResponse(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: String
)