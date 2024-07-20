package com.steve_md.smartmkulima.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

/**
 * This data model class accommodates both crop cycle and service cycle
 */

@Entity(tableName = "cycle")
@Parcelize
data class Cycle(
    @PrimaryKey  val farmId: String,
    val cropName: String,
    val startDate: String,
    val type: String, // "crop cycle" or "service cycle/livestock"
    val tasks: List<Tasks>
) : Parcelable


@Entity(tableName = "cycle_tasks")
@Parcelize
data class Tasks (
  @PrimaryKey  val taskName: String,
    val startDate: String,
    val endDate: String
) : Parcelable

