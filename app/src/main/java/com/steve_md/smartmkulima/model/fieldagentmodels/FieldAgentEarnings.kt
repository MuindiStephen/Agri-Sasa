package com.steve_md.smartmkulima.model.fieldagentmodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * A model data class to track the field agent's points
 * for registering / adding new farmers.
 */
@Entity(tableName = "field_agents_earnings")
data class FieldAgentEarnings(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val fieldAgentID: String,
    val points: Int = 0,
    val earnings: Double = 0.0
)