package com.steve_md.smartmkulima.model.fieldagentmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Store locally a field agent...
 */

data class FieldAgentUser(
    val id: Int = 0,
    val email: String,
    val password: String
)
