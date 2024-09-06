package com.steve_md.smartmkulima.model.financialdata

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Track total input costs / expenses and total revenue generated :)
 * For final records / summary to the farmer
 *
 * Then here will have the pie-chart on click to view more
 *
 */

@Parcelize
@Entity(tableName = "farm_summary_records")
data class FarmFinancialDataSummary(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val nameOfCropCycle: String,
    val totalInputCosts: String,
    val totalRevenueGenerated: String
) : Parcelable
