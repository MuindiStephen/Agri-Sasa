package com.steve_md.smartmkulima.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.steve_md.smartmkulima.model.financialdata.FarmFinancialDataSummary

@Dao
interface FarmSummaryRecordsDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSummaryRecord(farmFinancialDataSummary: FarmFinancialDataSummary)

    @Query("DELETE FROM `farm_summary_records`")
    suspend fun deleteAllSummary()

    @Query("SELECT * FROM farm_summary_records")
    fun getAllFarmFinancialDataSummary(): LiveData<List<FarmFinancialDataSummary>>
}