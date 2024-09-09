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


@Parcelize
data class AgroDealer(
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

@Parcelize
data class AgroDealerOffers(
    var productImageResId: Int,
    val productName: String,
    val originalPrice: String,
    val discountedPrice: String,
    val discountPercentage: String,
) : Parcelable
