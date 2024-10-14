package com.steve_md.smartmkulima.model.fieldagentmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

// A Field agent
// adds a new agrodealer with these attributes

@Entity(tableName = "fieldagent_added_agrodealers")
data class FieldAgentAddAgroDealerData(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val email: String,
    val phone: String,
    val location: String,
    val physicalLocationAddress: String,
    val agentId: String,   // field agent who registered this farmer
    val offers: String  // offer categories ie. select where applicable
)
