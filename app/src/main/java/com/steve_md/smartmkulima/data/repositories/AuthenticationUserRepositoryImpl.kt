//package com.steve_md.smartmkulima.data.repositories
//
//import com.steve_md.smartmkulima.data.remote.BaseRepositorySafeApiCall
//import com.steve_md.smartmkulima.data.remote.RetrofitApiService
//import com.steve_md.smartmkulima.model.requests.*
//
//class AuthenticationUserRepositoryImpl(
//    private val retrofitApiService :  RetrofitApiService
//) : AuthenticationUserRepository, BaseRepositorySafeApiCall() {
//
//    // To implement the interface AuthenticationUserRepository
//
//    // Login User
//    override suspend fun userLoginWithEmail(emailLoginRequest: EmailLoginRequest) = safeApiCall {
//        retrofitApiService.loginUserWithEmail(emailLoginRequest)
//    }
//
//    override suspend fun userRegisterWithEmail(emailSignUpRequest: EmailSignUpRequest) = safeApiCall {
//      retrofitApiService.registerUserWithEmail(emailSignUpRequest)
//    }
//
//
//
//
//
//    // TODO (not working for now)
//    override suspend fun userEmailOTPVerification(emailOTPRequest: EmailOTPRequest) = safeApiCall {
//       retrofitApiService.verifyUserWithEmail(emailOTPRequest.otp)
//    }
//
//    override suspend fun userLoginWithPhone(phoneLoginRequest: PhoneLoginRequest) = safeApiCall{
//        retrofitApiService.loginUserWithPhone(phoneLoginRequest)
//    }
//
//    override suspend fun userRegisterWithPhone(phoneSignUpRequest: PhoneSignUpRequest) = safeApiCall {
//        retrofitApiService.registerUserWithPhone(phoneSignUpRequest)
//    }
//
//    override suspend fun userPhoneOTPVerification(phoneOTPRequest: PhoneOTPRequest) = safeApiCall {
//        retrofitApiService.confirmPhoneOTP(phoneOTPRequest.otp)
//    }
//
//    override suspend fun recoverPasswordWithPhone(forgotPasswordWithPhone: ForgotPasswordWithPhone) = safeApiCall{
//        retrofitApiService.forgotPassword(forgotPasswordWithPhone)
//    }
//
//    override suspend fun validateForgotPasswordWithPhone(phoneOTPRequest: PhoneOTPRequest) = safeApiCall {
//        retrofitApiService.validateForgotPasswordWithPhoneOTP(phoneOTPRequest.otp)
//    }
//
//    override suspend fun changeForgotPasswordWithPhone(changePasswordRequest: ChangePasswordRequest) = safeApiCall{
//        retrofitApiService.forgotPasswordChangePassword(changePasswordRequest)
//    }
//
//
////    override suspend fun userEmailOTPVerification(emailOTPRequest: EmailOTPRequest): Resource<EmailOTPResponse> {
////        retrofitApiService.verifyUserWithEmail(emailOTPRequest)
////    }
//
//
//}