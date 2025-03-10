package com.steve_md.smartmkulima.data.remote

import com.steve_md.smartmkulima.model.voicebotresponse.BotOrdersResponse
import retrofit2.http.GET

interface BotOrderApiServices {

    @GET("api/Orders")
    suspend fun getAllBotOrders(): BotOrdersResponse
}