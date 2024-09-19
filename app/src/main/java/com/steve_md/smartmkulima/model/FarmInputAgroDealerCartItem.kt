package com.steve_md.smartmkulima.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * The Cart item for Agricultural inputs
 */
@Parcelize
data class FarmInputAgroDealerCartItem(
    val offerProduct: AgroDealerOffers,
    var quantity: Int = 1
): Parcelable