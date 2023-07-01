package com.steve_md.smartmkulima.data.repositories

import com.steve_md.smartmkulima.data.remote.FarmProduceApiService
import javax.inject.Inject

class FarmProduceRepository @Inject constructor(
    private val apiService: FarmProduceApiService
){

}