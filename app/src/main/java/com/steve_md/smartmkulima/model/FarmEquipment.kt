package com.steve_md.smartmkulima.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FarmEquipment(
    val id:Int,
    val name:String,
    val year:String,
    val priceHire:String,
    val imageUrl:String
) : Parcelable
