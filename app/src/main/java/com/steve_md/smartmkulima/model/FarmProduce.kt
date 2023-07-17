package com.steve_md.smartmkulima.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "produce")
data class FarmProduce(
    @PrimaryKey(autoGenerate = true) val id:Int,
    @ColumnInfo(name = "image") val productImageUrl:String,
    @ColumnInfo(name = "title") val productTitle:String,
    @ColumnInfo(name = "price") val productPrice:String
)
