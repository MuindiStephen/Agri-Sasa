package com.steve_md.smartmkulima.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.steve_md.smartmkulima.data.repositories.FarmCycleRepository
import com.steve_md.smartmkulima.data.repositories.FarmProduceRepository
import com.steve_md.smartmkulima.model.AgroDealerOffers
import com.steve_md.smartmkulima.model.FarmInputAgroDealerCartItem
import com.steve_md.smartmkulima.model.FarmProduce
import com.steve_md.smartmkulima.model.LocalFarmCycle
import com.steve_md.smartmkulima.model.NewFarmField
import com.steve_md.smartmkulima.model.OrderCheckoutByFarmer
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceExpenseRecords
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceRevenueRecords
import com.steve_md.smartmkulima.model.financialdata.FarmFinancialDataSummary
import com.steve_md.smartmkulima.utils.ApiStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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

    fun updateTaskStatus(status: String, cropName: String) = viewModelScope.launch {
        repository.updateTaskStatus(status, cropName)
    }

    fun updateToNewCommentsCropCycleCancelled(comments: String, cropName: String) = viewModelScope.launch {
        repository.updateToNewCommentsCropCycle(comments, cropName)
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



//    fun getCropData(cropName: String): LiveData<CropRecord?> {
//        return repository.getCropData(cropName)
//    }





    private val _selectedCrop = MutableLiveData<String>()
    val selectedCrop: LiveData<String> get() = _selectedCrop


    fun setSelectedCrop(cropName: String) {
        _selectedCrop.value = cropName
        Timber.d("Selected crop: $cropName")
    }

    // LiveData for total expenses and sales
    private val _totalExpensesForCrop = MutableLiveData<String>()
    val totalExpenseForCrop : LiveData<String> get() = _totalExpensesForCrop


    private val _totalSalesForCrop = MutableLiveData<String>()
    val totalSalesForCrop : LiveData<String> get() = _totalSalesForCrop


    // LiveData to expose calculated revenue
    val calculatedRevenue: LiveData<String?> = MutableLiveData()

    init {
        // Observe changes in selectedCrop and fetch data accordingly
        _selectedCrop.observeForever { cropName ->
            cropName?.let {
                fetchTotalExpensesForCrop(it)
                fetchTotalSalesForCrop(it)
            }
        }

        // Observe total expenses and sales to calculate revenue
        _totalExpensesForCrop.observeForever { expenses ->
            calculateRevenue()
        }

        _totalSalesForCrop.observeForever { sales ->
            calculateRevenue()
        }
    }

    private fun fetchTotalSalesForCrop(cropName: String) {

        viewModelScope.launch {
            repository.getTotalSalesForCrop(cropName).observeForever { sales ->
                _totalSalesForCrop.value = sales ?: "0.0"
            }
        }
    }

    private fun fetchTotalExpensesForCrop(cropName: String) {

        viewModelScope.launch {
            repository.getTotalExpensesForCrop(cropName).observeForever { expenses ->
                _totalExpensesForCrop.value = expenses ?: "0.0"
            }
        }

    }


    private fun calculateRevenue() {
        val expenses = _totalExpensesForCrop.value ?: 0.0
        val sales = _totalSalesForCrop.value ?: 0.0
        (calculatedRevenue as MutableLiveData).value =
            (sales.toString().toDouble() - expenses.toString().toDouble()).toString()
    }


    // Picking respective expenses records for cropCycle
    /*
    val totalExpenses: LiveData<Double?> = MediatorLiveData<Double?>().apply {
        addSource(_selectedCrop) { crop ->
            val expensesSource = repository.getTotalExpensesForCrop(crop)
            addSource(expensesSource) { expenseValue ->
                value = expenseValue
            }
        }
    }
    
     */

    /*
    // picking respective revenue records for cropCycle
    val totalSales: LiveData<Double?> = MediatorLiveData<Double?>().apply {
        addSource(_selectedCrop) { crop ->
            val salesSource = repository.getTotalSalesForCrop(crop)
            addSource(salesSource) { salesValue ->
                value = salesValue
            }
        }
    }
    
     */
    
    /*

    val calculatedRevenue: LiveData<Double> = MediatorLiveData<Double>().apply {
        var expenses: Double? = null
        var sales: Double? = null

        addSource(totalExpenses) { expenseValue ->
            expenses = expenseValue
            value = calculateRevenue(expenses, sales)
        }

        addSource(totalSales) { salesValue ->
            sales = salesValue
            value = calculateRevenue(expenses, sales)
        }
    }
    
     */

    


    /**
     * ADD TO CART - SUPPLIES / AGRO-DEALERS
     */
    private val _cart = MutableStateFlow<List<FarmInputAgroDealerCartItem>>(emptyList())
    val cart: StateFlow<List<FarmInputAgroDealerCartItem>>
        get() =_cart.asStateFlow()


    // Adding the AgroDealer Deal to cart
    // pass also agrodealer ID here :)
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

//                Timber.tag("MainViewModel-CART")
//                    .d("CART ASSOCIATED WITH AGRO-DEALER WITH UNIQUE ID: {}")

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



    // Ui SwipeToRefresh
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.emit(true)
            delay(1000)
            _isRefreshing.emit(false)
        }
    }


    // Get order based on the agrodealer ID
    fun ordersByAgroDealerID (agroDealerId: String) : LiveData<List<OrderCheckoutByFarmer>> =
        repository.getSpecificOrdersForAgrodealerID(agroDealerId)

    // Adding a new order
    fun saveOrder(order: OrderCheckoutByFarmer) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.saveOrders(order)
        }
    }

    fun updateOrderStatus(newStatus: String, agroDealerId: String) = viewModelScope.launch {
        repository.updateOrderStatus(newStatus, agroDealerId)
    }

}

// UI State for managing FarmProduce State
data class FarmProduceState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val farmProduce: List<FarmProduce> = emptyList()
)