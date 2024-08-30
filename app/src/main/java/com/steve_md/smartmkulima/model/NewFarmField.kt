package com.steve_md.smartmkulima.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Add new farm details
 */
@Entity(tableName = "farm_blocks")
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
)
