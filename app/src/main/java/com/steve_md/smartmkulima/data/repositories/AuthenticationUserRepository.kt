package com.steve_md.smartmkulima.data.repositories

import com.steve_md.smartmkulima.model.requests.*
import com.steve_md.smartmkulima.model.responses.*
import com.steve_md.smartmkulima.utils.Resource


/**
 * Communicates with remote data source (from web service/server)
 */

interface AuthenticationUserRepository {

    suspend fun userLoginWithEmail(emailLoginRequest: EmailLoginRequest) : Resource<EmailLoginResponse>

    suspend fun userRegisterWithEmail(emailSignUpRequest: EmailSignUpRequest) : Resource<EmailSignUpResponse>





    // TODO (Not working for now)
    suspend fun userEmailOTPVerification(emailOTPRequest: EmailOTPRequest) : Resource<EmailOTPResponse>

    suspend fun userLoginWithPhone(phoneLoginRequest: PhoneLoginRequest) : Resource<PhoneLoginResponse>

    suspend fun userRegisterWithPhone(phoneSignUpRequest: PhoneSignUpRequest) : Resource<PhoneSignUpResponse>

    suspend fun userPhoneOTPVerification(phoneOTPRequest: PhoneOTPRequest) : Resource<PhoneOTPResponse>

    suspend fun recoverPasswordWithPhone(forgotPasswordWithPhone: ForgotPasswordWithPhone) : Resource<PhoneSignUpResponse>

    suspend fun validateForgotPasswordWithPhone(phoneOTPRequest: PhoneOTPRequest) : Resource<PhoneOTPResponse>

    suspend fun changeForgotPasswordWithPhone(changePasswordRequest: ChangePasswordRequest) : Resource<ChangePasswordResponse>
}
