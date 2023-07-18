package com.steve_md.smartmkulima.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id:Int,
    @ColumnInfo(name = "amount") val amount:Double,
    @ColumnInfo(name = "transactionDate") val transactionDateTime:String
)
