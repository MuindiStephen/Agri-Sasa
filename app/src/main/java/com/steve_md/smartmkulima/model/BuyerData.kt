package com.steve_md.smartmkulima.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query


@Entity(tableName = "buyer_cart")
data class BuyerCart(
    @PrimaryKey(autoGenerate = true)
    val cartId: Long = 0,
    val productPrice: String,
    val productTitle: String,
    val productImage: String
)

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
