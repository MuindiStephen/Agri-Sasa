package com.steve_md.smartmkulima.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.steve_md.smartmkulima.model.fieldagentmodels.FieldAgentEarnings


@Dao
interface FieldAgentEarningsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveOrUpdatePoints(fieldAgentEarnings: FieldAgentEarnings)

    @Query("SELECT * FROM field_agents_earnings WHERE fieldAgentID = :agentId LIMIT 1")
    suspend fun getPointsByAgentId(agentId: String): FieldAgentEarnings? // Only expecting a single result.
         // save loads of data and improve query performance.
}