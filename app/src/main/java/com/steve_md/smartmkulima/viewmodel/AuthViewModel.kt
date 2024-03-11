package com.steve_md.smartmkulima.viewmodel

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.steve_md.smartmkulima.data.repositories.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.steve_md.smartmkulima.utils.Event
import com.steve_md.smartmkulima.utils.Resource
import kotlinx.coroutines.launch

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : ViewModel() {

    private val _registerStatus = MutableLiveData<Event<Resource<AuthResult>>>()
    val registerStatus: LiveData<Event<Resource<AuthResult>>>
        get() = _registerStatus

    private val _loginStatus = MutableLiveData<Event<Resource<AuthResult>>>()
    val loginStatus: LiveData<Event<Resource<AuthResult>>>
        get() = _loginStatus


    fun registerUser(
        username: String,
        email: String,
        confirmPassword: String,
        password: String,
    ) {
        var error =
            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                "Empty Strings"
            } else if (confirmPassword != password) {
                "Passwords do not match"
            } else if (confirmPassword.length < 8) {
                "Confirm password too short"
            } else if (password.length < 8 && confirmPassword.length < 8) {
                "Password too short"
            }  else null

        error?.let {
            _registerStatus.postValue(Event(Resource.Error(it)))
            return
        }
        _registerStatus.postValue(Event(Resource.Loading()))

        viewModelScope.launch(dispatcher) {
            val result = authRepository.register(username, email,password, confirmPassword)
            _registerStatus.postValue(Event(result))
        }
    }

    fun loginUser(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _loginStatus.postValue(Event(Resource.Error("Empty Strings")))
        } else {
            _loginStatus.postValue(Event(Resource.Loading()))
            viewModelScope.launch(dispatcher) {
                val result = authRepository.login(email, password)
                _loginStatus.postValue(Event(result))
            }
        }
    }

}