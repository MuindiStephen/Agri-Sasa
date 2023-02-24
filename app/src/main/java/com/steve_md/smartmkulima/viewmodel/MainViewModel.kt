package com.steve_md.smartmkulima.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steve_md.smartmkulima.data.remote.RetrofitApiService
import com.steve_md.smartmkulima.data.repositories.UserRepository
import com.steve_md.smartmkulima.model.responses.UserResponseItem
import com.steve_md.smartmkulima.utils.Resource
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val getUserRepository:UserRepository = UserRepository(RetrofitApiService.getApiClient())

    // Get the user directly from the database
//    private val _userValue = MutableStateFlow<Resource<UserResponseItem>?>(null)
//    val userValue: StateFlow<Resource<UserResponseItem>?>
//    get() = _userValue
//
//    fun getUser() = viewModelScope.launch {
//        _userValue.value = Resource.Loading
//        _userValue.value = getUserRepository.getUser()
//    }


    private val _user : MutableLiveData<Resource<UserResponseItem>> = MutableLiveData()
    val user: LiveData<Resource<UserResponseItem>>
    get() = _user

    fun getUser() = viewModelScope.launch {
        _user.value = Resource.Loading
        _user.value = getUserRepository.getUser()
    }


}