package com.steve_md.smartmkulima.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steve_md.smartmkulima.data.remote.RetrofitApiService
import com.steve_md.smartmkulima.data.repositories.AuthenticationUserRepository
import com.steve_md.smartmkulima.data.repositories.AuthenticationUserRepositoryImpl
import com.steve_md.smartmkulima.model.requests.*
import com.steve_md.smartmkulima.model.responses.*
import com.steve_md.smartmkulima.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthenticationViewModel : ViewModel() {

    private val authenticationUserRepository:AuthenticationUserRepository =
        AuthenticationUserRepositoryImpl(RetrofitApiService.getApiClient())

    private var apiJob:Job? = null

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

    //Email Login Observable
    private val _loginResult = MutableStateFlow<Resource<EmailLoginResponse>?>(null)
    val loginResult: StateFlow<Resource<EmailLoginResponse>?> get() = _loginResult

    // Email Register Observable
    private val _registerResult = MutableStateFlow<Resource<EmailSignUpResponse>?>(null)
    val registerResult: StateFlow<Resource<EmailSignUpResponse>?>
        get() = _registerResult

    // email Otp observable
    private val _otpResult = MutableStateFlow<Resource<EmailOTPResponse>?>(null)
    val otpResult : StateFlow<Resource<EmailOTPResponse>?>
    get() = _otpResult

    // Phone Login Observable
    private val _loginRes = MutableStateFlow<Resource<PhoneLoginResponse>?>(null)
    val loginRes: StateFlow<Resource<PhoneLoginResponse>?> get() = _loginRes

    // Phone Register Observable
    private val _registerRes = MutableStateFlow<Resource<PhoneSignUpResponse>?>(null)
    val registerRes: StateFlow<Resource<PhoneSignUpResponse>?> get() = _registerRes

    // Phone OTP Observable
    private val _otpRes = MutableStateFlow<Resource<PhoneOTPResponse>?>(null)
    val otpRes:StateFlow<Resource<PhoneOTPResponse>?>
    get() = _otpRes

    // Forgot Password
    private val _forgotPassword = MutableStateFlow<Resource<PhoneSignUpResponse>?>(null)
    val forgotPassword:StateFlow<Resource<PhoneSignUpResponse>?>
        get() = _forgotPassword

    // Change Password
    private val _changePassword = MutableStateFlow<Resource<ChangePasswordResponse>?>(null)
    val changePassword:StateFlow<Resource<ChangePasswordResponse>?>
        get() = _changePassword




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

    fun loginUserUsingPhone(phone:String,password:String) = viewModelScope.launch {
        _loginRes.value = authenticationUserRepository.userLoginWithPhone(PhoneLoginRequest(password = password, phone = phone))
    }

    fun registerUserUsingPhone(username:String, phone:String,password:String, confirmPassword: String) = viewModelScope.launch {
        _registerRes.value = authenticationUserRepository.userRegisterWithPhone(PhoneSignUpRequest(username = username, phone = phone, password = password, confirmPassword = confirmPassword))
    }

    fun confirmPhoneOTP(otp: String) = viewModelScope.launch {
        _otpRes.value = authenticationUserRepository.userPhoneOTPVerification(PhoneOTPRequest(otp = otp))
    }

    fun forgotPassword(phone:String) = viewModelScope.launch {
        _forgotPassword.value = authenticationUserRepository.recoverPasswordWithPhone(ForgotPasswordWithPhone(phone = phone))
    }






}