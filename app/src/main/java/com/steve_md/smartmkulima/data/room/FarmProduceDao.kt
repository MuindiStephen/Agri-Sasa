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

    @Query("SELECT * FROM produce")
    fun getAllFarmProduce() : Flow<List<FarmProduce>> // Emits values anytime data changes in the Room-db
    // instead of livedata

    // When the device is online, instead cached data will be deleted and replaced with another one
    @Query("DELETE FROM produce")
    suspend fun deleteAllFarmProduce()


    // Get items from the local database upon searching
    // Returns a flow of result type
    @Query("SELECT * FROM produce WHERE title LIKE :searchQuery OR price  LIKE :searchQuery")
    fun searchDatabase(searchQuery: String) : Flow<List<FarmProduce>>
}