package com.steve_md.smartmkulima.model

/**
 * Light weight model to display farm produce to the UI
 */
data class FarmProduce(
    val id:Int,
    val productImageUrl:String,
    val productTitle:String,
    val productPrice:Double
)
