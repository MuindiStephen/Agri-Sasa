package com.steve_md.smartmkulima.model.fieldagentmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "field_agents_earnings")
data class FieldAgentEarnings(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val points: Int = 0,
    val earnings: Double = 0.0
)