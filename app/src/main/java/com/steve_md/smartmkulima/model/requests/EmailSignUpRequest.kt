package com.steve_md.smartmkulima.model.requests


import com.google.gson.annotations.SerializedName

data class EmailSignUpRequest(
    @SerializedName("confirmPassword")
    val confirmPassword: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("name")
    val name: String
)