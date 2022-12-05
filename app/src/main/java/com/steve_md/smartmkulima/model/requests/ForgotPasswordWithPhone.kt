package com.steve_md.smartmkulima.model.requests


import com.google.gson.annotations.SerializedName

data class ForgotPasswordWithPhone(
    @SerializedName("phone")
    val phone: String
)