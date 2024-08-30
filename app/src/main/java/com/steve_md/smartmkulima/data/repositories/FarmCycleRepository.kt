package com.steve_md.smartmkulima.data.repositories

import androidx.lifecycle.LiveData
import com.steve_md.smartmkulima.data.room.AppDatabase
import com.steve_md.smartmkulima.data.room.FarmFieldsDao
import com.steve_md.smartmkulima.data.room.GAPDao
import com.steve_md.smartmkulima.data.room.LocalFarmCycleDao
import com.steve_md.smartmkulima.model.Cycle
import com.steve_md.smartmkulima.model.LocalFarmCycle
import com.steve_md.smartmkulima.model.NewFarmField
import com.steve_md.smartmkulima.utils.apiRequestByResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Farm cycles repository
 * Manual creating of cycle for automation
 */
class FarmCycleRepository @Inject constructor(
    database: AppDatabase
) {
    private val gapDao: GAPDao = database.gapDao()

    private val localCycleDao: LocalFarmCycleDao = database.localFarmCycleDao()

    private val farmFieldDao: FarmFieldsDao =  database.farmfielddao()

    suspend fun insertCycle(cycle: LocalFarmCycle) = apiRequestByResource {
        localCycleDao.insertLocalFarmCycle(cycle)
    }

    val allCycles : Flow<List<Cycle>> = gapDao.getAllCycle()


    fun getAllCycles(): LiveData<List<LocalFarmCycle>> {
        return localCycleDao.getAllFarmCycle()
    }

    suspend fun updateTaskStatus(status: String) {
        localCycleDao.updateTaskStatus(status)
    }

    fun getAllFarmFields(): LiveData<List<NewFarmField>> {
        return farmFieldDao.getAllFarmFields()
    }

    suspend fun insertFarmField(farmField: NewFarmField) = apiRequestByResource {
        farmFieldDao.saveFarmField(farmField)
    }

}