package com.steve_md.smartmkulima.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceExpenseRecords
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceRevenueRecords

@Dao
interface FarmCycleRevenueRecordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCycleRevenue(farmFinanceRevenueRecords: FarmFinanceRevenueRecords)

    @Query("DELETE FROM `farm_after_harvest_records`")
    suspend fun deleteCycleRevenues()

    @Query("SELECT * FROM farm_after_harvest_records")
    fun getAllCycleRevenues(): LiveData<List<FarmFinanceRevenueRecords>>
}