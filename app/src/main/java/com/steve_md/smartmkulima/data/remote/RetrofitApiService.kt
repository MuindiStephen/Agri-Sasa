package com.steve_md.smartmkulima.data.remote

import com.steve_md.smartmkulima.model.FarmProduce
import com.steve_md.smartmkulima.model.requests.*
import com.steve_md.smartmkulima.model.requests.buyers.BuyerRegisterRequest
import com.steve_md.smartmkulima.model.requests.fieldagent.FieldAgentRegisterRequest
import com.steve_md.smartmkulima.model.responses.*
import com.steve_md.smartmkulima.model.responses.buyer.BuyerLoginResponse
import com.steve_md.smartmkulima.model.responses.buyer.BuyerRegisterResponse
import com.steve_md.smartmkulima.model.responses.fieldagent.FieldAgentLoginResponse
import com.steve_md.smartmkulima.model.responses.fieldagent.FieldAgentRegisterResponse
import com.steve_md.smartmkulima.utils.Constants.LOGIN_END_POINT
import com.steve_md.smartmkulima.utils.Constants.REGISTER_END_POINT
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import retrofit2.http.Path
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


    companion object {
        fun getApiClient() : RetrofitApiService {
            return ApiClient.retrofit.create(RetrofitApiService::class.java)
        }
    }

    // TODO (Not working for now)
    @POST("/signup/email/confirm")
    suspend fun verifyUserWithEmail(
        @Query("OTP") otp : String
    ) : EmailOTPResponse

    @POST("/signup/phone")
    suspend fun registerUserWithPhone(
        @Body phoneSignUpRequest: PhoneSignUpRequest
    ) : PhoneSignUpResponse

    @POST("/signup/phone/confirm")
    suspend fun confirmPhoneOTP(
        @Query("OTP") sms:String
    ) : PhoneOTPResponse

    // Sign in user using phone number
    @POST("/login/phone")
    suspend fun loginUserWithPhone(
        @Body phoneLoginRequest: PhoneLoginRequest
    ) : PhoneLoginResponse

    @POST("/forgotpassword/request")
    suspend fun forgotPassword(
        @Body forgotPasswordWithPhone: ForgotPasswordWithPhone
    ) : PhoneSignUpResponse

    @POST("/forgotpassword/validate")
    suspend fun validateForgotPasswordWithPhoneOTP(
        @Query("OTP") fpOTP: String
    ) : PhoneOTPResponse

    @POST("/forgotpassword/changepassword")
    suspend fun forgotPasswordChangePassword(
        @Body changePasswordRequest: ChangePasswordRequest
    ) : ChangePasswordResponse


    // To get the user from the database
     @GET("/signup/usersByEmail/")
     suspend fun getUser(): UserResponseItem


    /**
     * FIELD AGENTS AUTHENTICATION
     */
        @POST("resource/registerfieldagent")
        suspend fun registerAFieldAgent(
           @Body fieldAgentRegisterRequest: FieldAgentRegisterRequest
        ): FieldAgentRegisterResponse

       @GET("resource/registerfieldagent")
       suspend fun loginFieldAgent() : FieldAgentLoginResponse

    @POST("resource/buyers")
    suspend fun registerBuyer(
        @Body buyerRegisterRequest: BuyerRegisterRequest
    ): BuyerRegisterResponse

    @GET("resource/buyers")
    suspend fun loginBuyer() : BuyerLoginResponse


    // Sample Deleting of a farmer
    /**
    @HTTP(
    method = "DELETE",
    path = "/v1/delete/",
    hasBody = true
    )
    suspend fun deleteAgroDealer(
        @Body requestBody: RequestBody,
        @Path("id") id: Int
    ): ResponseBody
    */

}