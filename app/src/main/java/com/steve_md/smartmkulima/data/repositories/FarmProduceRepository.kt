package com.steve_md.smartmkulima.data.repositories

import androidx.room.withTransaction
import com.steve_md.smartmkulima.data.remote.FarmProduceApiService
import com.steve_md.smartmkulima.data.room.AppDatabase
import com.steve_md.smartmkulima.model.FarmProduce
import com.steve_md.smartmkulima.utils.networkBoundResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FarmProduceRepository @Inject constructor(
    private val farmProduceApiService: FarmProduceApiService,
    private val appDatabase: AppDatabase
) {
    private val farmProduceDao = appDatabase.farmProduceDao()

    fun getAllFarmProduce() = networkBoundResource(

        // Logic to query data from database - [offline cache storage]
        // Querying cached data from the local db
        query = {
            farmProduceDao.getAllFarmProduce()
        },

        // Logic to fetch data from the api
        fetch = {
            delay(2000)
            farmProduceApiService.getAllFarmProduce()
        },

        // Logic to save the fetched resultType data from api to the local db
        saveFetchResult = { produce ->
            appDatabase.withTransaction {
                farmProduceDao.deleteAllFarmProduce()
                farmProduceDao.insertFarmProduce(produce)
            }
        },

        // Logic to determine if networking call should be made
        shouldFetch = { products ->
            products.isEmpty()
        }

    )

    // Fetching products from the remote api web service
    fun searchDatabase(searchQuery: String): Flow<List<FarmProduce>> {
        return farmProduceDao.searchDatabase(searchQuery)
    }
}