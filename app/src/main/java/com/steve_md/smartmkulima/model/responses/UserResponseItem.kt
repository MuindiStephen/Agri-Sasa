package com.steve_md.smartmkulima.model.responses


import com.google.gson.annotations.SerializedName

data class UserResponseItem(
    @SerializedName("accountNonExpired")
    val accountNonExpired: Boolean,
    @SerializedName("accountNonLocked")
    val accountNonLocked: Boolean,
    @SerializedName("appUserRole")
    val appUserRole: String,
    @SerializedName("authorities")
    val authorities: List<Authority>,
    @SerializedName("confirmPassword")
    val confirmPassword: Any,
    @SerializedName("credentialsNonExpired")
    val credentialsNonExpired: Boolean,
    @SerializedName("email")
    val email: String,
    @SerializedName("enabled")
    val enabled: Boolean,
    @SerializedName("id")
    val id: Int,
    @SerializedName("locked")
    val locked: Boolean,
    @SerializedName("password")
    val password: String,
    @SerializedName("userName")
    val userName: String,
    @SerializedName("username")
    val username: String
)