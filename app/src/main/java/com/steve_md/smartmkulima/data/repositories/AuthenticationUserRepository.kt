package com.steve_md.smartmkulima.data.repositories

import com.steve_md.smartmkulima.model.requests.EmailLoginRequest
import com.steve_md.smartmkulima.model.requests.EmailSignUpRequest
import com.steve_md.smartmkulima.model.responses.EmailLoginResponse
import com.steve_md.smartmkulima.model.responses.EmailSignUpResponse
import com.steve_md.smartmkulima.utils.Resource


// Communicates with remote data source (from web service/server)

interface AuthenticationUserRepository {
    suspend fun userLoginWithEmail(emailLoginRequest: EmailLoginRequest) : Resource<EmailLoginResponse>

    suspend fun userRegisterWithEmail(emailSignUpRequest: EmailSignUpRequest) : Resource<EmailSignUpResponse>
}
