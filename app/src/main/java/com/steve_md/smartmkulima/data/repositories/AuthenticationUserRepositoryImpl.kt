package com.steve_md.smartmkulima.data.repositories

import com.steve_md.smartmkulima.data.remote.BaseRepositorySafeApiCall
import com.steve_md.smartmkulima.data.remote.RetrofitApiService
import com.steve_md.smartmkulima.model.requests.EmailLoginRequest
import com.steve_md.smartmkulima.model.requests.EmailSignUpRequest

class AuthenticationUserRepositoryImpl(
    private val retrofitApiService :  RetrofitApiService
) : AuthenticationUserRepository, BaseRepositorySafeApiCall(){

    // To implement the interface AuthenticationUserRepository

    // Login User
    override suspend fun userLoginWithEmail(emailLoginRequest: EmailLoginRequest) = safeApiCall {
        retrofitApiService.loginUserWithEmail(emailLoginRequest)
    }

    override suspend fun userRegisterWithEmail(emailSignUpRequest: EmailSignUpRequest) = safeApiCall{
      retrofitApiService.registerUserWithEmail(emailSignUpRequest)
    }


}