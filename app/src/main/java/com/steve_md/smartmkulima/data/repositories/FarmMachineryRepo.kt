package com.steve_md.smartmkulima.data.repositories

import androidx.lifecycle.LiveData
import com.steve_md.smartmkulima.data.room.FarmMachineryDao
import com.steve_md.smartmkulima.model.FarmMachinery
import javax.inject.Inject

class FarmMachineryRepository @Inject constructor(
    private val dao: FarmMachineryDao
) {
    val allMachinery: LiveData<List<FarmMachinery>> = dao.getAllMachinery()

    suspend fun insertMachinery(machinery: FarmMachinery) {
        dao.insertMachinery(machinery)
    }

    suspend fun getUnsyncedMachinery(): List<FarmMachinery> {
        return dao.getUnsyncedMachinery()
    }

    suspend fun updateMachinery(machinery: FarmMachinery) {
       // dao.updateMachinery(machinery)
    }
}
