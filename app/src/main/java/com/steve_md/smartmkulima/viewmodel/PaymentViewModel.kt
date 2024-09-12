package com.steve_md.smartmkulima.viewmodel

import PaymentRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steve_md.smartmkulima.payment.mpesa.dto.StkPushSuccessResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val paymentRepository: PaymentRepository
) : ViewModel() {

    val accessToken: MutableLiveData<String> = MutableLiveData()
    val stkPushResponse: MutableLiveData<Result<StkPushSuccessResponse>> = MutableLiveData()

    // initiate DarajaApiClient
    fun initiateDarajaApiClient() {
        viewModelScope.launch {
            paymentRepository.initiateDarajaApiClient()
        }
    }

    // Get access token from the repository
    fun getAccessToken() {
        viewModelScope.launch {
            try {
                val token = paymentRepository.getAccessToken()
                accessToken.postValue(token)
            } catch (e: Exception) {
                Timber.tag(TAG).e(e, "Failed to fetch access token")
            }
        }
    }

    // Perform STK Push
    fun performSTKPush(phoneNumber: String, amount: String) {
        viewModelScope.launch {
            try {
                val response = paymentRepository.performSTKPush(phoneNumber, amount)
                stkPushResponse.postValue(Result.success(response))
            } catch (e: Exception) {
                stkPushResponse.postValue(Result.failure(e))
                Timber.tag(TAG).e(e, "Failed to perform STK Push")
            }
        }
    }

     suspend fun savePaymentTransactionToDB(amount: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                paymentRepository.savePaymentTransaction(amount)
            }
        }
    }

    companion object {
        private const val TAG = "PaymentViewModel"
    }
}
