package com.steve_md.smartmkulima.utils

import okhttp3.ResponseBody

// Wrapping all the API responses
//Handle API Success and Error responses
sealed class Resource<out T> (){
    data class Success<out T>(val value: T) : Resource<T>()
    data class Error(
        val isNetworkError: Boolean,
        val errorCode: Int?,
        val errorBody: ResponseBody?
    ) : Resource<Nothing>()
    object Loading : Resource<Nothing>()
}
