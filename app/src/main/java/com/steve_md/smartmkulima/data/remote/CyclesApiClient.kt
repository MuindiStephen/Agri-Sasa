package com.steve_md.smartmkulima.data.remote

import com.steve_md.smartmkulima.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CyclesApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.getStringBaseUrlDevelopment())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: CyclesApiService by lazy {
        retrofit.create(CyclesApiService::class.java)
    }
}