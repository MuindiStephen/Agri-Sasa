package com.steve_md.smartmkulima.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.steve_md.smartmkulima.model.NewFarmField

@Dao
interface FarmFieldsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFarmField(newFarmField: NewFarmField)

    @Query("DELETE FROM `farm_blocks`")
    suspend fun deleteAllFarmFields()

    @Query("SELECT * FROM farm_blocks")
    fun getAllFarmFields(): LiveData<List<NewFarmField>>
}