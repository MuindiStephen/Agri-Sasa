package com.steve_md.smartmkulima.model.responses.fieldagent

data class FieldAgentRegisterResponse(
    val createdAt: String,
    val email: String,
    val id: Int,
    val password: String,
    val updatedAt: String
)