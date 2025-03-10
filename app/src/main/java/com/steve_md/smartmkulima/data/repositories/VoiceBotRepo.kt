package com.steve_md.smartmkulima.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.steve_md.smartmkulima.data.remote.BotOrderApiServices
import com.steve_md.smartmkulima.model.voicebotresponse.BotOrdersResponse
import timber.log.Timber
import javax.inject.Inject

class VoiceBotRepo @Inject constructor(
    private val apiService: BotOrderApiServices
){
    fun fetchVoiceBotOrders(): LiveData<BotOrdersResponse> = liveData {
        try {
            Timber.d("Success: trying to pull voice bot data")
            val response = apiService.getAllBotOrders()
            emit(response) // Emit the response as LiveData
        } catch (e: Exception) {
            Timber.d("::Error:: Could not pull voice orders :WHY: ${e.message}")

        }
    }
}