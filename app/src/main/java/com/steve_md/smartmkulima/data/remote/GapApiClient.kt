package com.steve_md.smartmkulima.data.remote

import com.steve_md.smartmkulima.model.GAP
import com.steve_md.smartmkulima.utils.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GapApiClient {
    private val retrofit = Retrofit.Builder()
        .baseUrl(Constants.GAP_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: GAPsAPiService by lazy {
        retrofit.create(GAPsAPiService::class.java)
    }
}