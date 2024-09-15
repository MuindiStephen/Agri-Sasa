package com.steve_md.smartmkulima.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.steve_md.smartmkulima.model.NewFarmField
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceExpenseRecords

@Dao
interface FarmCycleExpensesRecordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCycleExpense(farmFinanceExpenseRecords: FarmFinanceExpenseRecords)

    @Query("DELETE FROM `farm_expenses_records`")
    suspend fun deleteCycleExpenses()

    @Query("SELECT * FROM farm_expenses_records")
    fun getAllCycleExpenses(): LiveData<List<FarmFinanceExpenseRecords>>


    @Query("SELECT amountSpent FROM farm_expenses_records WHERE nameOfCropCycle = :cropName")
    fun getTotalExpensesForCrop(cropName: String): LiveData<Double?>
}