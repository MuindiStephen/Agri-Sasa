package com.steve_md.smartmkulima.data.remote

import com.steve_md.smartmkulima.model.GAP
import retrofit2.Call
import retrofit2.http.GET

interface GAPsAPiService {

    @GET("goodagriculturalpractices")
    fun getAllGAPs() : Call<ArrayList<GAP>>
}