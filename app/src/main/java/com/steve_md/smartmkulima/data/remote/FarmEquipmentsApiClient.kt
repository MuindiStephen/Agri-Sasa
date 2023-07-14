package com.steve_md.smartmkulima.data.remote

import com.steve_md.smartmkulima.utils.Constants.FARM_EQUIPMENTS_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object FarmEquipmentsApiClient {

    private val retrofit = Retrofit.Builder()
        .baseUrl(FARM_EQUIPMENTS_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: FarmEquipmentApiService by lazy {
        retrofit.create(FarmEquipmentApiService::class.java)
    }
}