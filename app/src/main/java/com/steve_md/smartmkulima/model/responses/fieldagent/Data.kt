package com.steve_md.smartmkulima.model.responses.fieldagent

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fieldagents")
data class Data(
    val createdAt: String,
    val email: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val password: String,
    val updatedAt: String
)