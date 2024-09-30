package com.steve_md.smartmkulima.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.steve_md.smartmkulima.model.LocalFarmCycle
import kotlinx.coroutines.flow.Flow


@Dao
interface LocalFarmCycleDao {

    // Updating the crop cycle
    @Insert
    suspend fun insertLocalFarmCycle(localFarmCycle: LocalFarmCycle)

    @Update
    suspend fun updateLocalFarmCycle(localFarmCycle: LocalFarmCycle)

    @Query("SELECT * FROM localcycle WHERE cropName = :cropName")
    suspend fun getFarmCycleBYName(cropName: String): LocalFarmCycle?

    @Query("SELECT * FROM localcycle")
    fun getAllFarmCycle(): LiveData<List<LocalFarmCycle>>

    @Query("UPDATE localcycle SET status = :status WHERE cropName =:cropName")
    suspend fun updateTaskStatus(status: String, cropName: String)

    @Query("UPDATE localcycle SET comments = :comments WHERE cropName =:cropName")
    suspend fun updateToNewComments(comments: String, cropName: String)
}