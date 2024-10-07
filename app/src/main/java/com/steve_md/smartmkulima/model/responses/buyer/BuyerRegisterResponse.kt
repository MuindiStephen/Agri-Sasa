package com.steve_md.smartmkulima.model.responses.buyer

data class BuyerRegisterResponse(
    val createdAt: String,
    val email: String,
    val id: Int,
    val password: String,
    val updatedAt: String
)