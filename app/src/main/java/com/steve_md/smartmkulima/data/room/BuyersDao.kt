package com.steve_md.smartmkulima.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.steve_md.smartmkulima.model.responses.buyer.Data
import kotlinx.coroutines.flow.Flow

@Dao
interface BuyersDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoadedBuyers(
        buyersList: List<Data>
    )

    @Query("SELECT * FROM buyers")
    fun getAllBuyers() : Flow<List<Data>>


    @Query("DELETE FROM buyers")
    suspend fun deleteBuyersLocally()

}