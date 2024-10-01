package com.steve_md.smartmkulima.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.steve_md.smartmkulima.model.OrderCheckoutByFarmer
import com.steve_md.smartmkulima.model.fieldagentmodels.FieldAgentAddAgroDealerData

@Dao
interface FieldAgentAddAgrodealerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun fieldAgentAddANewAgrodealer(fieldAgentAddAgroDealerData: FieldAgentAddAgroDealerData)

    @Query("SELECT * FROM fieldagent_added_agrodealers")
    fun getAllFieldAgentAddedAgrodealers(): LiveData<List<FieldAgentAddAgroDealerData>>
}