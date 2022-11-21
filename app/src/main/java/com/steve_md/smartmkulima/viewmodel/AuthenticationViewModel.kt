package com.steve_md.smartmkulima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steve_md.smartmkulima.data.remote.RetrofitApiService
import com.steve_md.smartmkulima.data.repositories.AuthenticationUserRepository
import com.steve_md.smartmkulima.data.repositories.AuthenticationUserRepositoryImpl
import com.steve_md.smartmkulima.model.requests.EmailLoginRequest
import com.steve_md.smartmkulima.model.requests.EmailOTPRequest
import com.steve_md.smartmkulima.model.requests.EmailSignUpRequest
import com.steve_md.smartmkulima.model.responses.EmailLoginResponse
import com.steve_md.smartmkulima.model.responses.EmailOTPResponse
import com.steve_md.smartmkulima.model.responses.EmailSignUpResponse
import com.steve_md.smartmkulima.utils.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel : ViewModel() {

    private val authenticationUserRepository:AuthenticationUserRepository =
        AuthenticationUserRepositoryImpl(RetrofitApiService.getApiClient())

    /**
     * view model will communicate with repository
     * repository sends and gets data from the web server api
     * and then use the data to update the UI
     *
     * **/


    // use kotlin flows instead of live data

    /**
     * Using  mutable state flow
     */

    // Login Observable
    private val _loginResult = MutableStateFlow<Resource<EmailLoginResponse>?>(null)
    val loginResult: StateFlow<Resource<EmailLoginResponse>?> get() = _loginResult

    // Register Observable
    private val _registerResult = MutableStateFlow<Resource<EmailSignUpResponse>?>(null)
    val registerResult: StateFlow<Resource<EmailSignUpResponse>?>
        get() = _registerResult

    // Otp observable
    private val _otpResult = MutableStateFlow<Resource<EmailOTPResponse>?>(null)
    val otpResult : StateFlow<Resource<EmailOTPResponse>?>
    get() = _otpResult



    fun loginUserUsingEmail(email: String, password: String) = viewModelScope.launch {
        _loginResult.value = authenticationUserRepository
            .userLoginWithEmail(EmailLoginRequest(email = email, password = password))
    }
    fun registerUsingEmail(username:String, email:String, password: String, confirmPassword:String) = viewModelScope.launch {
        _registerResult.value = authenticationUserRepository
            .userRegisterWithEmail(EmailSignUpRequest(username = username, email = email, password = password, confirmPassword = confirmPassword))
    }

    fun verifyEmailOtp(otp:String) = viewModelScope.launch {
        _otpResult.value = authenticationUserRepository.userEmailOTPVerification(EmailOTPRequest(otp = otp))
    }






}