package com.steve_md.smartmkulima.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.steve_md.smartmkulima.model.OrderCheckoutByFarmer
import com.steve_md.smartmkulima.model.fieldagentmodels.FieldAgentEarnings


@Dao
interface FieldAgentEarningsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFieldAgentEarnings(fieldAgentEarnings: FieldAgentEarnings)

    @Query("UPDATE field_agents_earnings SET points =:newPoints, earnings = :newEarnings")
    suspend fun updateFieldAgentEarnings(newPoints: Int, newEarnings: Double)

    @Query("SELECT * FROM field_agents_earnings")
    fun fetchAllFieldAgentEarnings(): LiveData<FieldAgentEarnings>
}