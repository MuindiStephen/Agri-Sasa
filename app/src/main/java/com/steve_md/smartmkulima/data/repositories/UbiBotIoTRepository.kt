package com.steve_md.smartmkulima.data.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.steve_md.smartmkulima.data.remote.UbiBotIoTWebService
import com.steve_md.smartmkulima.model.ubibot_iot.UbiBotAllResponse
import com.steve_md.smartmkulima.model.ubibot_iot.UbiBotResponse
import timber.log.Timber
import javax.inject.Inject

class UbiBotIoTRepository @Inject constructor(
    private val ubiBotIoTWebService: UbiBotIoTWebService
) {
    fun fetchLatestEntryUbiBotData(): LiveData<UbiBotResponse> = liveData {
        try {
            Timber.d("Success: trying to pull IoT data")
            val response = ubiBotIoTWebService.fetchLatestEntryUbiBotData()
            emit(response) // Emit the response as LiveData
        } catch (e: Exception) {
            Timber.d("::Error:: Could not pull IoT data :WHY: ${e.message}")
        }
    }

    fun fetchAllUbiBotData(): LiveData<List<UbiBotAllResponse>> = liveData {
        try {
            Timber.d("::Success:: Trying to pull IoT data")
            val response = ubiBotIoTWebService.fetchAllUbiBotData()

            if (response.isEmpty()) {
                Timber.d("Response is empty, emitting an empty list")
                emit(emptyList<UbiBotAllResponse>())
            } else {
                emit(response)
            }
        } catch (e: Exception) {
            Timber.d("::Failed:: Error: Could not pull IoT data ::WHY:: ${e.message}:")
            emit(emptyList<UbiBotAllResponse>())
        }
    }
}