package com.steve_md.smartmkulima.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.steve_md.smartmkulima.model.responses.fieldagent.Data
import com.steve_md.smartmkulima.model.responses.fieldagent.FieldAgentLoginResponse
import com.steve_md.smartmkulima.model.responses.fieldagent.FieldAgentRegisterResponse
import kotlinx.coroutines.flow.Flow

@Dao
interface FieldAgentUserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLoadedFieldAgents(
        fieldAgentsList: List<Data>
    )

    @Query("SELECT * FROM fieldagents")
    fun getAllFieldAgents() : Flow<List<Data>>


    @Query("DELETE FROM fieldagents")
    suspend fun deleteFieldAgentsLocally()

}