package com.steve_md.smartmkulima.data.repositories

import com.google.firebase.auth.AuthResult
import com.steve_md.smartmkulima.model.requests.fieldagent.FieldAgentRegisterRequest
import com.steve_md.smartmkulima.model.responses.fieldagent.FieldAgentRegisterResponse
import com.steve_md.smartmkulima.utils.Resource
import com.steve_md.smartmkulima.utils.ResourceNetwork
import com.steve_md.smartmkulima.utils.apiRequestByResource

interface AuthRepository {
    suspend fun register(username: String, email: String,  password: String, confirmPassword: String) : Resource<AuthResult>
    suspend fun login(email: String, password: String) : Resource<AuthResult>
}