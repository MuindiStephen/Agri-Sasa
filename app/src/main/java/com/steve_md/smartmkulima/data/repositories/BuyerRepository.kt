package com.steve_md.smartmkulima.data.repositories

import androidx.lifecycle.LiveData
import androidx.room.withTransaction
import com.steve_md.smartmkulima.data.remote.RetrofitApiService
import com.steve_md.smartmkulima.data.room.AppDatabase
import com.steve_md.smartmkulima.data.room.BuyersDao
import com.steve_md.smartmkulima.data.room.FieldAgentUserDao
import com.steve_md.smartmkulima.model.BuyerCart
import com.steve_md.smartmkulima.model.BuyerCartDao
import com.steve_md.smartmkulima.model.requests.buyers.BuyerRegisterRequest
import com.steve_md.smartmkulima.model.requests.fieldagent.FieldAgentRegisterRequest
import com.steve_md.smartmkulima.utils.apiRequestByResource
import com.steve_md.smartmkulima.utils.networkBoundResource
import javax.inject.Inject

class BuyerRepository @Inject constructor(
    private val apiService: RetrofitApiService,
    private val appDatabase: AppDatabase
) {

    private val buyerDao: BuyersDao = appDatabase.buyerDao()
    private val buyerCartDao: BuyerCartDao = appDatabase.buyerCartDao()

    // First fetch buyers from api then save it to local room db persistent.
    suspend fun getAllBuyersByLogin() = networkBoundResource(

        query = {
            buyerDao.getAllBuyers()
        },
        fetch = {
            // delay(2000)
            apiService.loginBuyer() // fetch
        },
        saveFetchResult = { buyerLoginRes ->
            appDatabase.withTransaction {
                buyerDao.deleteBuyersLocally()  // delete when on network for data consistency and not-duplicated
                buyerDao.insertLoadedBuyers(buyerLoginRes.data) // then insert again after fetch
            }
        },
        shouldFetch = { buyersList ->
            buyersList.isEmpty()
        }
    )

    suspend fun registerBuyer(buyerRegisterRequest: BuyerRegisterRequest) = apiRequestByResource {
        apiService.registerBuyer(buyerRegisterRequest)
    }


    suspend fun deleteAllCartItems() {
        buyerCartDao.deleteAllCartItems()
    }

    suspend fun insertProductToCartLineItem(cartEntity: BuyerCart) {
        buyerCartDao.insertToCartLineItem(cartEntity)
    }

    suspend fun deleteAnItemFromCartLine(cartEntity: BuyerCart) {
        buyerCartDao.deleteAnItemFromCart(cartEntity)
    }

    fun getCartLineItems() : LiveData<List<BuyerCart>> {
        return buyerCartDao.getCartItems()
    }

    // Update the quantity of an item in the cart
    suspend fun updateQuantity(cartItem: BuyerCart, newQuantity: Int) {
        val updatedItem = cartItem.copy(quantity = newQuantity)
        buyerCartDao.updateItem(updatedItem)
    }

    // Retrieve the total quantity and total price
    suspend fun getCartSummary(): Pair<Int, Double> {
        val totalQuantity = buyerCartDao.getTotalQuantity() ?: 0
        val totalPrice = buyerCartDao.getTotalPrice() ?: 0.0
        return Pair(totalQuantity, totalPrice)
    }

}