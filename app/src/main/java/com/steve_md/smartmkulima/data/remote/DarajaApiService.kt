package com.steve_md.smartmkulima.data.remote

import com.steve_md.smartmkulima.payment.mpesa.dto.AuthorizationResponse
import com.steve_md.smartmkulima.payment.mpesa.dto.StkPushRequest
import com.steve_md.smartmkulima.payment.mpesa.dto.StkPushSuccessResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Mobile Money  - Mpesa Daraja API
 */
interface DarajaApiService {
    @POST("mpesa/stkpush/v1/processrequest")
    fun sendPush(
        @Body
        stkPushRequest: StkPushRequest
    ) : Call<StkPushSuccessResponse>

    @GET("oauth/v1/generate?grant_type=client_credentials")
    fun getAccessToken() : Call<AuthorizationResponse>
}