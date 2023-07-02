package com.steve_md.smartmkulima.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// TDDO
// NOT USED in this code
class SplashViewModel : ViewModel() {
    private val _splashValue = MutableStateFlow<Boolean>(false)
    val splashValue: StateFlow<Boolean> get() = _splashValue

    fun setValue(){
        android.os.Handler().postDelayed({
            _splashValue.value = true
        }, 3000)
    }
}