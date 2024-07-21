package com.steve_md.smartmkulima.data.room

import androidx.room.*
import com.steve_md.smartmkulima.model.Cycle
import com.steve_md.smartmkulima.model.GAP
import kotlinx.coroutines.flow.Flow

@Dao
interface GAPDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGAP(cycle: Cycle)

    @Query("SELECT * FROM cycle")
    fun getAllCycle(): Flow<List<Cycle>>

    @Delete
     suspend fun deleteGAP(cycle: Cycle)
}
