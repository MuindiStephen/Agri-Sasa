package com.steve_md.smartmkulima.model

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
    val buildingLocation: String
) : Parcelable
