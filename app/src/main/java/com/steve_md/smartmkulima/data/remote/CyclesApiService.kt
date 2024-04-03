package com.steve_md.smartmkulima.data.remote

import com.steve_md.smartmkulima.model.Cycle
import retrofit2.Call
import retrofit2.http.GET

/**
 * Stephen Muindi @2024-04-03
 * +2547-40495903
 */
interface CyclesApiService {
        @GET("cycles")
        fun getAllFarmCycles() : Call<ArrayList<Cycle>>
}