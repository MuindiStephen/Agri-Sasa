package com.steve_md.smartmkulima.data.remote

import com.steve_md.smartmkulima.model.FarmEquipment
import retrofit2.Call
import retrofit2.http.GET

interface FarmEquipmentApiService {
    @GET("farmequipments")
    fun getAllEquipments() : Call<ArrayList<FarmEquipment>>
}