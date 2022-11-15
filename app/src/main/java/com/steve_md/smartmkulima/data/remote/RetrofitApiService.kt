package com.steve_md.smartmkulima.data.remote

import com.steve_md.smartmkulima.model.requests.EmailLoginRequest
import com.steve_md.smartmkulima.model.requests.EmailSignUpRequest
import com.steve_md.smartmkulima.model.responses.EmailLoginResponse
import com.steve_md.smartmkulima.model.responses.EmailSignUpResponse
import com.steve_md.smartmkulima.utils.Constants.LOGIN_END_POINT
import com.steve_md.smartmkulima.utils.Constants.REGISTER_END_POINT
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitApiService {
    // Sign up with email
    @POST(REGISTER_END_POINT)
    suspend fun registerUserWithEmail(
        @Body emailSignUpRequest: EmailSignUpRequest
    ) : EmailSignUpResponse

    // Login or sign in with email
    @POST(LOGIN_END_POINT)
    suspend fun loginUserWithEmail(
        @Body emailLoginRequest: EmailLoginRequest
    ) : EmailLoginResponse

    companion object {
        fun getApiClient() : RetrofitApiService {
            return ApiClient.retrofit.create(RetrofitApiService::class.java)
        }
    }

}