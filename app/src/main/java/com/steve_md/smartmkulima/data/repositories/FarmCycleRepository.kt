package com.steve_md.smartmkulima.data.repositories

import com.steve_md.smartmkulima.data.room.AppDatabase
import com.steve_md.smartmkulima.data.room.GAPDao
import com.steve_md.smartmkulima.data.room.LocalFarmCycleDao
import com.steve_md.smartmkulima.model.Cycle
import com.steve_md.smartmkulima.model.GAP
import com.steve_md.smartmkulima.model.LocalFarmCycle
import com.steve_md.smartmkulima.utils.apiRequestByResource
import com.steve_md.smartmkulima.utils.safeCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Farm cycles repository
 * Manual creating of repositories
 */
class FarmCycleRepository @Inject constructor(
    database: AppDatabase
) {
    private val gapDao: GAPDao = database.gapDao()

    private val localCycleDao: LocalFarmCycleDao = database.localFarmCycleDao()

    suspend fun insertCycle(cycle: LocalFarmCycle) = apiRequestByResource {
        localCycleDao.insertLocalFarmCycle(cycle)
    }

    val allCycles : Flow<List<Cycle>> = gapDao.getAllCycle()

    val allLocalFarmCyle: Flow<List<LocalFarmCycle>> = flow {
        localCycleDao.getAllFarmCycle()
    }

}