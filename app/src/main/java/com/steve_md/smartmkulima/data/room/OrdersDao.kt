package com.steve_md.smartmkulima.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.steve_md.smartmkulima.model.OrderCheckoutByFarmer
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceRevenueRecords


@Dao
interface OrdersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrder(orderCheckoutByFarmer: OrderCheckoutByFarmer)

    @Query("SELECT * FROM orders WHERE agrodealerID =:agrodealerID")
    fun getSpecificOrdersForAgroDealerID(agrodealerID: String): LiveData<List<OrderCheckoutByFarmer>>

    @Query("UPDATE orders SET orderStatus = :newOrderStatus WHERE agrodealerID =:agrodealerID")
    suspend fun updateOrderStatus(newOrderStatus: String, agrodealerID: String)

    @Query("SELECT * FROM orders")
    fun fetchAllOrdersToTheFarmer(): LiveData<List<OrderCheckoutByFarmer>>
}