//package com.steve_md.smartmkulima.data.remote
//
//import com.steve_md.smartmkulima.utils.Resource
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//import retrofit2.HttpException
//
///**
// * Safe APi Call through HTTP which is not secure
// */
//abstract class BaseRepositorySafeApiCall() {
//
//    // use of coroutines for asynchronous programming -> without non blocking execution
//    suspend fun <T> safeApiCall(apiCall: suspend () -> T): Resource<T> {
//
//        /**
//         * try and catch block functions with return body
//         */
//
//        //  Execute all the Api calls asynchronously by use of coroutines
//
//        //  Makes it a suspending block
//        return withContext(Dispatchers.IO) {
//
//            try {
////                val response = apiCall.invoke()
////                Resource.Success(response)
//                Resource.Success(apiCall.invoke())
//            } catch (e: Throwable) {
//                when (e){
//                    is HttpException -> {
//                        Resource.Error(false, e.code(), e.response()?.errorBody())
//                    }
//                    else -> {
//                        Resource.Error(true,null, null)
//                    }
//                }
//            }
//        }
//
//    }
//
//
//}