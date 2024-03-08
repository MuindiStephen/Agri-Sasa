package com.steve_md.smartmkulima.model

/**
 * User request to firebase
 */
data class User(
    val uid: String? = "",
    val username: String,
    val email:String,
    val password:String,
    val confirmPassword:String
)