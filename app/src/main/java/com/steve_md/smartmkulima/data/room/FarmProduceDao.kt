package com.steve_md.smartmkulima.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.steve_md.smartmkulima.model.FarmProduce
import kotlinx.coroutines.flow.Flow


@Dao
interface FarmProduceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFarmProduce(products: List<FarmProduce>)

    @Query("SELECT * FROM products")
    fun getAllProducts() : Flow<List<FarmProduce>>

    // When the device is online, instead cached data will be deleted and replaced with another one
    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()


    // Get items from the local database upon searching
    // Returns a flow of result type
    @Query("SELECT * FROM products WHERE title LIKE :searchQuery OR price  LIKE :searchQuery")
    fun searchDatabase(searchQuery: String) : Flow<List<FarmProduce>>
}