package com.steve_md.smartmkulima.model.voicebotresponse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class BotOrdersResponse(
    val success: Boolean,
    val message: String,
    val data: List<Order>
)

@Parcelize
data class Order(
    val orderId: Int,
    val orderTotal: Int,
    val paymentMethod: String,
    val orderDate: String,
    val items: List<OrderItem>
): Parcelable

@Parcelize
data class OrderItem(
    val productId: Int,
    val orderId: Int,
    val productName: String,
    val pricePerKg: Int,
    val quantity: Int,
    val total: Int
): Parcelable