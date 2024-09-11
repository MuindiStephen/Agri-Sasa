package com.steve_md.smartmkulima.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.steve_md.smartmkulima.data.repositories.FarmCycleRepository
import com.steve_md.smartmkulima.data.repositories.FarmProduceRepository
import com.steve_md.smartmkulima.model.AgroDealer
import com.steve_md.smartmkulima.model.AgroDealerOffers
import com.steve_md.smartmkulima.model.Cycle
import com.steve_md.smartmkulima.model.FarmInputAgroDealerCartItem
import com.steve_md.smartmkulima.model.FarmProduce
import com.steve_md.smartmkulima.model.LocalFarmCycle
import com.steve_md.smartmkulima.model.NewFarmField
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceExpenseRecords
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceRevenueRecords
import com.steve_md.smartmkulima.model.financialdata.FarmFinancialDataSummary
import com.steve_md.smartmkulima.utils.ApiStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject


/**
 * The viewmodel that interacts with the Ui
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val farmProduceRepository: FarmProduceRepository,
    private val repository: FarmCycleRepository
) : ViewModel() {

    private val _produce = MutableSharedFlow<FarmProduceState>()
    val produce: SharedFlow<FarmProduceState> = _produce


    val allCycles: LiveData<List<LocalFarmCycle>> = repository.getAllCycles()

    fun addCropCycle(cycle: LocalFarmCycle) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.insertCycle(cycle)
        }
    }

    fun updateTaskStatus(status: String) = viewModelScope.launch {
        repository.updateTaskStatus(status)
    }

    init {
        getAllFarmProduce()
    }




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


    // All farm fields
    val allFarmFields: LiveData<List<NewFarmField>> = repository.getAllFarmFields()

    // Adding a new farm field / farm
    fun addFarmField(farmField: NewFarmField) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.insertFarmField(farmField)
        }
    }

    fun addFarmCycleExpense(farmFinanceExpenseRecords: FarmFinanceExpenseRecords) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.insertNewCycleExpense(farmFinanceExpenseRecords)
        }
    }

    fun addFarmCycleRevenue(farmFinanceRevenueRecords: FarmFinanceRevenueRecords) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.insertNewCycleRevenue(farmFinanceRevenueRecords)
        }
    }

    // deleting expenses records
    fun deleteFarmCycleExpense() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.deleteAllCycleExpenses()
        }
    }

    // Deleting revenue records
    fun deleteFarmCycleRevenue() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.deleteAllCycleRevenues()
        }
    }

    val allFarmCycleExp: LiveData<List<FarmFinanceExpenseRecords>> = repository.getAllCycleExpenses()

    val allFarmCycleRevenues: LiveData<List<FarmFinanceRevenueRecords>> = repository.getAllCycleRevenues()


    // Get a list of all farm records summary
    val allSummaryRecords : LiveData<List<FarmFinancialDataSummary>> = repository.getAllSummaryRecords()

    // Adding a new farm summary record
    fun addFarmSummaryRecords(farmFinancialDataSummary: FarmFinancialDataSummary) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.saveFarmSummaryRecord(farmFinancialDataSummary)
        }
    }

    // Deleting Farm Summary Records
    fun deleteFarmSummaryRecords() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.deleteAllSummaryRecords()
        }
    }

    /**
     * ADD TO CART - SUPPLIES / AGRO-DEALERS
     */
    private val _cart = MutableStateFlow<List<FarmInputAgroDealerCartItem>>(emptyList())
    val cart: StateFlow<List<FarmInputAgroDealerCartItem>>
        get() =_cart.asStateFlow()


    // Adding the AgroDealer Deal to cart
    fun addToCart(agroDealerOffers: AgroDealerOffers) {
        viewModelScope.launch {
            val currentCart = _cart.value.toMutableList()
            val existingItem = currentCart.find {
                it.offerProduct.id == agroDealerOffers.id
            }
            if (existingItem != null) {
                existingItem.quantity++
                Timber.tag("MainViewModel-CART")
                    .d("%s%s", "Increased quantity for " + agroDealerOffers.productName + ". New quantity: ", existingItem.quantity)
            } else {
               currentCart.add(FarmInputAgroDealerCartItem(agroDealerOffers))
                Timber.tag("MainViewModel-CART")
                    .d("Added New Item To Cart: %s", agroDealerOffers.productName)
                Timber.tag("MainViewModel-CART-value-log1").d(_cart.value.toString())
                Timber.tag("MainViewModel-CART-value-log2").d(cart.value.toString())
                Timber.tag("MainViewModel-CART-value-log3").d(currentCart.toString())

            }
            _cart.value = currentCart

            Timber.tag("X-MainViewModel-CART-value-logA").d(_cart.value.toString())
            Timber.tag("Y-MainViewModel-CART-value-logB").d(cart.value.toString())
            Timber.tag("Z-MainViewModel-CART-value-logC").d(currentCart.toString())

            logCartContents()
        }
    }


    // Log cart contents...
    private fun logCartContents() {
        Timber.tag("MainViewModel-CART").d("Current cart contents:")
        _cart.value.forEach { item ->
            Timber.tag("MainViewModel-CART")
                .d("%s%s", item.offerProduct.productName + " - Quantity: ", item.quantity)
        }
    }

    // Getting Cart count
    fun getCartItemCount(): Int {
        return _cart.value.sumOf { it.quantity }
    }

    // Increasing Cart Quantity Items
    fun increaseQuantity(offers: AgroDealerOffers) {
        viewModelScope.launch {
            val currentCart = _cart.value.toMutableList()
            val item = currentCart.find { it.offerProduct.id == offers.id }
            if (item != null) {
                item.quantity++
                _cart.value = currentCart
            }
        }
    }

    // Decreasing Cart Quantity Items
    fun decreaseQuantity(offers: AgroDealerOffers) {
        viewModelScope.launch {
            val currentCart = _cart.value.toMutableList()
            val item = currentCart.find { it.offerProduct.id == offers.id }
            if (item != null) {
                if (item.quantity > 1) {
                    item.quantity--
                } else {
                    currentCart.remove(item)
                }
                _cart.value = currentCart
            }
        }
    }


    // Removing Cart Items
    fun removeFromCart(offers: AgroDealerOffers) {
        viewModelScope.launch {
            val currentCart = _cart.value.toMutableList()
            currentCart.removeAll { it.offerProduct.id == offers.id}
            _cart.value = currentCart
        }
    }


    // Clearing Items
    fun clearCart() {
        _cart.value = emptyList()
    }
}

// UI State for managing FarmProduce State
data class FarmProduceState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val farmProduce: List<FarmProduce> = emptyList()
)