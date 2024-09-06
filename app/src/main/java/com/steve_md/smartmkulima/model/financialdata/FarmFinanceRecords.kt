package com.steve_md.smartmkulima.model.financialdata

import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Records for each individual crop Cycle
 * it records each expense that's made.
 *
 * ACT AS A DIGITAL NOTEBOOK FOR FARM RECORDS TO THE FARMER
 */

// during entire crop cycle & before harvest
@Entity(tableName = "farm_expenses_records")
data class FarmFinanceExpenseRecords (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val nameOfCropCycle: String,
    val nameOfExpense: String,
    val amountSpent: String,
    val whichTask: String,
    val dateOfThisFinancialRecord: String
)

// after harvest
@Entity(tableName = "farm_after_harvest_records")
data class FarmFinanceRevenueRecords (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val cropCycleName: String,
    val harvestedBags: String,
    val soldBags: String,
    val revenueAfterSales: String
)