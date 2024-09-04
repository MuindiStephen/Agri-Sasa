package com.steve_md.smartmkulima.model.financialdata

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Track total input costs/ expenses and total revenue generated
 * for final records / summary to the farmer
 *
 * then here will have the pie-chart on click to view more
 */

@Entity(tableName = "farm_summary_records")
data class FarmFinancialDataSummary(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nameOfCropCycle: String,
    val totalInputCosts: String,
    val totalRevenueGenerated: String
)
