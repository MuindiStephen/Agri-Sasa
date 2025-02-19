package com.steve_md.smartmkulima.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "farm_machinery")
data class FarmMachinery(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val price: Double,
    val productImage: String, // Store image as a file path or base64 string
    val isSynced: Boolean = false // Flag to track sync status
): Parcelable
