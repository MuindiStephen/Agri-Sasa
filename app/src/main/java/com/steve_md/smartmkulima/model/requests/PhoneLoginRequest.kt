package com.steve_md.smartmkulima.model.requests


import com.google.gson.annotations.SerializedName

data class PhoneLoginRequest(
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String
)