//package com.steve_md.smartmkulima.data.repositories
//
//import com.steve_md.smartmkulima.data.remote.BaseRepositorySafeApiCall
//import com.steve_md.smartmkulima.data.remote.RetrofitApiService
//
//class UserRepository (
//       private val getUserApi : RetrofitApiService
//    )  : BaseRepositorySafeApiCall() {
//
//    suspend fun getUser() = safeApiCall {
//        getUserApi.getUser()
//    }
//}