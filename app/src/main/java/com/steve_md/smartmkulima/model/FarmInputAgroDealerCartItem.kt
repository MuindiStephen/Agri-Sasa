package com.steve_md.smartmkulima.model

/**
 * The Cart item for Agricultural inputs
 */
data class FarmInputAgroDealerCartItem(
    val offerProduct: AgroDealerOffers,
    var quantity: Int = 1
)