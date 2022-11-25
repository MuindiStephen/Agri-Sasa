package com.steve_md.smartmkulima.model.requests


import com.google.gson.annotations.SerializedName

data class PhoneSignUpRequest(
    @SerializedName("confirmPassword")
    val confirmPassword: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("username")
    val username: String
)