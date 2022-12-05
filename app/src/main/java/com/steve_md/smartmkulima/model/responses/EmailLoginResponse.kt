package com.steve_md.smartmkulima.model.responses


import com.google.gson.annotations.SerializedName

data class EmailLoginResponse(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("email")
    val email: String
)