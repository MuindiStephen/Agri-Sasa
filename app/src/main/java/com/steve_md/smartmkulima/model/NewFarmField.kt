package com.steve_md.smartmkulima.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * Add new farm details
 */
@Entity(tableName = "farm_blocks")
@Parcelize
data class NewFarmField(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var farmName: String,
    var cropName: String,
    val farmDescription: String,  // eg. estate name
    var farmSizeInHa: String,    // in ha
    var estimatedNumberOfFarmersPerDay: String,
    var ownershipType: String,
    val season: String,
    val year: String,
    val countyLocationOfTheFarm: String
): Parcelable
