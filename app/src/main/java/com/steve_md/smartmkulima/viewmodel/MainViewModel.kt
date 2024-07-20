package com.steve_md.smartmkulima.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.steve_md.smartmkulima.data.repositories.FarmCycleRepository
import com.steve_md.smartmkulima.data.repositories.FarmProduceRepository
import com.steve_md.smartmkulima.model.Cycle
import com.steve_md.smartmkulima.model.FarmProduce
import com.steve_md.smartmkulima.model.LocalFarmCycle
import com.steve_md.smartmkulima.utils.ApiStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val farmProduceRepository: FarmProduceRepository,
    private val repository: FarmCycleRepository
) : ViewModel() {


    val allCycles: LiveData<List<LocalFarmCycle>> = repository.allLocalFarmCyle.asLiveData()

    fun addCropCycle(cycle: LocalFarmCycle) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.insertCycle(cycle)
        }
    }


    init {
        getAllFarmProduce()
    }

    private val _produce = MutableSharedFlow<FarmProduceState>()
    val produce: SharedFlow<FarmProduceState> = _produce


    private fun getAllFarmProduce() {
        viewModelScope.launch {
            _produce.emit(
                FarmProduceState(
                    isLoading = true,
                    error = null,
                    farmProduce = emptyList()
                )
            )

            farmProduceRepository.getAllFarmProduce().collectLatest { result ->
                when (result) {
                    is ApiStates.Error -> {
                        _produce.emit(
                            FarmProduceState(
                                isLoading = false,
                                error = result.error?.message ?: "Unknown Error Occurred"
                            )
                        )

                        Timber.e("Get all Farm Produce: ${result.error?.message ?: "Unknown Error Occurred"}")
                    }
                    is ApiStates.Success -> {
                        _produce.emit(
                            FarmProduceState(
                                isLoading = false,
                                farmProduce = result.data!!
                            )
                        )

                        Timber.e("Get All Farm Produce: ${result.data}")
                    }
                    else -> {}
                }
            }
        }
    }
}

// UI State
data class FarmProduceState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val farmProduce: List<FarmProduce> = emptyList()
)