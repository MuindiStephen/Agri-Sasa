package com.steve_md.smartmkulima.model.requests


import com.google.gson.annotations.SerializedName

data class ChangePasswordRequest(
    @SerializedName("newPassword")
    val newPassword: String
)