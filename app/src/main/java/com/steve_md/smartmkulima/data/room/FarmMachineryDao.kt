package com.steve_md.smartmkulima.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.steve_md.smartmkulima.model.FarmMachinery

@Dao
interface FarmMachineryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMachinery(machinery: FarmMachinery)

    @Query("SELECT * FROM farm_machinery")
    fun getAllMachinery(): LiveData<List<FarmMachinery>>

    @Query("SELECT * FROM farm_machinery WHERE isSynced = 0")
    suspend fun getUnsyncedMachinery(): List<FarmMachinery>

    @Query("UPDATE farm_machinery SET isSynced = 1 WHERE id = :machineryId")
    suspend fun updateSyncStatus(machineryId: String)

    @Update
    suspend fun updateMachinery(machinery: FarmMachinery)
}
