package com.steve_md.smartmkulima.data.repositories

import androidx.room.withTransaction
import com.steve_md.smartmkulima.data.remote.RetrofitApiService
import com.steve_md.smartmkulima.data.room.AppDatabase
import com.steve_md.smartmkulima.data.room.FieldAgentUserDao
import com.steve_md.smartmkulima.model.requests.fieldagent.FieldAgentRegisterRequest
import com.steve_md.smartmkulima.utils.apiRequestByResource
import com.steve_md.smartmkulima.utils.networkBoundResource
import javax.inject.Inject

class FieldAgentsRepository @Inject constructor(
    private val api: RetrofitApiService,
    private val appDatabase: AppDatabase
) {

    private val fieldAgentDao: FieldAgentUserDao = appDatabase.fieldAgentUserDao()

    // First fetch field agents from api then save it to local room db persistent.
    suspend fun getAllFieldAgentsByLogin() = networkBoundResource (

        query = {
                fieldAgentDao.getAllFieldAgents()
        },
        fetch = {
            // delay(2000)
            api.loginFieldAgent() // fetch
        },
        saveFetchResult = { fieldAgentResponse ->
            appDatabase.withTransaction {
                fieldAgentDao.deleteFieldAgentsLocally()  // delete when on network for data consistency and not-duplicated
                fieldAgentDao.insertLoadedFieldAgents(fieldAgentResponse.data) // then insert again after fetch
            }
        },
        shouldFetch = { fieldAgentsList->
            fieldAgentsList.isEmpty()
        }
    )

    suspend fun registerFieldAgent(fieldAgentRegisterRequest: FieldAgentRegisterRequest) = apiRequestByResource {
        api.registerAFieldAgent(fieldAgentRegisterRequest)
    }
}