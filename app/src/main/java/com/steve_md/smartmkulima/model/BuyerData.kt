package com.steve_md.smartmkulima.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update


@Entity(tableName = "buyer_cart")
data class BuyerCart(
    @PrimaryKey(autoGenerate = true)
    val cartId: Long = 0,
    val productPrice: String,
    val productTitle: String,
    val productImage: String,
    val quantity: Int = 1 // Default quantity is 1
) {
    // Calculate the total price based on quantity and product price
    val totalPrice: Double
        get() = productPrice.toDouble() * quantity
}

@Dao
interface BuyerCartDao {
    @Insert
    suspend fun insertToCartLineItem(cartLineItem: BuyerCart)

    // Delete all cart items
    @Query("DELETE FROM buyer_cart")
    suspend fun deleteAllCartItems()

    // Remove an item from the cart
    @Delete
    suspend fun deleteAnItemFromCart(cartEntity: BuyerCart)

    // Get access to all cart Items
    @Query("SELECT * FROM buyer_cart ORDER BY cartId DESC")
    fun getCartItems(): LiveData<List<BuyerCart>>

    @Update
    suspend fun updateItem(cartItem: BuyerCart)

    // Query to calculate the total quantity of items
    @Query("SELECT SUM(quantity) FROM buyer_cart")
    suspend fun getTotalQuantity(): Int?

    // Query to calculate the total price across all items in the cart
    @Query("SELECT SUM(productPrice * quantity) FROM buyer_cart")
    suspend fun getTotalPrice(): Double?
}

/**
 * A Mapper
 * * We are mapping FarmProduce data class into BuyerCart data class
 */
fun FarmProduce.toCartEntity() : BuyerCart {
    return BuyerCart(
        productPrice = productPrice,
        productTitle = productTitle,
        productImage = productImageUrl
    )
}
