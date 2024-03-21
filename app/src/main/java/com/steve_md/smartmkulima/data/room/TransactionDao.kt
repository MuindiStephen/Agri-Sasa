package com.steve_md.smartmkulima.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.steve_md.smartmkulima.model.Transaction

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveTransaction(transaction: Transaction)

    @Query("SELECT * FROM `transactions` ORDER by id ASC")
    fun getAllTransactions(): List<Transaction>

    @Query("SELECT * FROM `transactions` WHERE id = :id")
    fun getOnlyOneUserTransaction(id:Int): LiveData<Transaction>

    @Query("DELETE FROM `transactions`")
    suspend fun deleteAllTransactions()
}