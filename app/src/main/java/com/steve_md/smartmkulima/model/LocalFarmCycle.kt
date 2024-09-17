package com.steve_md.smartmkulima.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "localcycle")
@Parcelize
data class LocalFarmCycle(
    val farmName: String,
    @PrimaryKey val cropName: String,
    val startDate: String,
    val tasks: List<LocalTasks>,
    var status: String = "Upcoming", // Crop Cycle Statuses => In Progress, Upcoming, Overdue, Done
    val comments: String = "Bad weather"
) : Parcelable


@Entity(tableName = "cycle_tasks")
@Parcelize
data class LocalTasks (
    @PrimaryKey val taskName: String,
    val startDate: String,
    val endDate: String
) : Parcelable
