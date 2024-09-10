package com.steve_md.smartmkulima.model

import android.health.connect.datatypes.units.Percentage
import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

/**
 * A data class to represent agro-dealer data / supplier data
 */
data class AgroDealerData(
    val name:String,
    val latitude:Double,
    val longitude:Double
)

/**
 * Agro-Dealer
 */
@Parcelize
data class AgroDealer(
    val id: Int,
    val name: String,
    val phone: String,
    val email: String,
    val latitude: Double,
    val longitude:Double,
    val servicesOffered: String,
    val categories: String,
    val leasingOptionsAvailable: String,
    val leasingDetails: String,
    val buildingLocation: String,
    val offers: List<AgroDealerOffers>
) : Parcelable

/**
 * Agro-Dealer Offers
 */
@Parcelize
data class AgroDealerOffers(
    val id: Int,
    var productImageResId: Int,
    val productName: String,
    val originalPrice: Double,
    val discountedPrice: Double,
    val discountPercentage: String,
) : Parcelable
