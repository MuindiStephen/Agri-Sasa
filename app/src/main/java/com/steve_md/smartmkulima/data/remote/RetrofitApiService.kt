package com.steve_md.smartmkulima.data.remote

import com.steve_md.smartmkulima.model.requests.EmailLoginRequest
import com.steve_md.smartmkulima.model.requests.EmailSignUpRequest
import com.steve_md.smartmkulima.model.responses.EmailLoginResponse
import com.steve_md.smartmkulima.model.responses.EmailOTPResponse
import com.steve_md.smartmkulima.model.responses.EmailSignUpResponse
import com.steve_md.smartmkulima.model.responses.UserResponseItem
import com.steve_md.smartmkulima.utils.Constants.LOGIN_END_POINT
import com.steve_md.smartmkulima.utils.Constants.REGISTER_END_POINT
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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

    @POST("/signup/email/confirm")
    suspend fun verifyUserWithEmail(
        @Query("OTP") otp : String
    ) : EmailOTPResponse


     @GET("/signup/usersByEmail/")
     suspend fun getUser(): UserResponseItem

    companion object {
        fun getApiClient() : RetrofitApiService {
            return ApiClient.retrofit.create(RetrofitApiService::class.java)
        }
    }

}