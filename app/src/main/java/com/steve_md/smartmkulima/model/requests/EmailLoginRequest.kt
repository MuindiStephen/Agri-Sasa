package com.steve_md.smartmkulima.model.requests


import com.google.gson.annotations.SerializedName

data class EmailLoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)