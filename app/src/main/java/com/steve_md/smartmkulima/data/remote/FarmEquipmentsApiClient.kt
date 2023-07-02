package com.steve_md.smartmkulima.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FarmEquipmentsApiClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl("")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api by lazy {
        retrofit.create(FarmEquipmentApiService::class.java)
    }
}