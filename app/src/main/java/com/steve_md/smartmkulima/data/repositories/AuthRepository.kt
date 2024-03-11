package com.steve_md.smartmkulima.data.repositories

import com.google.firebase.auth.AuthResult
import com.steve_md.smartmkulima.utils.Resource

interface AuthRepository {
    suspend fun register(username: String, email: String,  password: String, confirmPassword: String) : Resource<AuthResult>
    suspend fun login(email: String, password: String) : Resource<AuthResult>
}