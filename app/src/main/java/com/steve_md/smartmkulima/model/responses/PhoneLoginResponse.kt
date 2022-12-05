package com.steve_md.smartmkulima.model.responses


import com.google.gson.annotations.SerializedName

data class PhoneLoginResponse(
    @SerializedName("accessToken")
    val accessToken: String
)