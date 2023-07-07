package com.steve_md.smartmkulima.data.remote

import com.steve_md.smartmkulima.model.FarmProduce
import retrofit2.http.GET

interface FarmProduceApiService {
    @GET("farmproduce")
    suspend fun getAllFarmProduce() : ArrayList<FarmProduce>
}