package com.steve_md.smartmkulima.data.repositories

import androidx.lifecycle.LiveData
import com.steve_md.smartmkulima.data.room.AppDatabase
import com.steve_md.smartmkulima.data.room.FarmCycleExpensesRecordsDao
import com.steve_md.smartmkulima.data.room.FarmCycleRevenueRecordsDao
import com.steve_md.smartmkulima.data.room.FarmFieldsDao
import com.steve_md.smartmkulima.data.room.FarmSummaryRecordsDao
import com.steve_md.smartmkulima.data.room.FieldAgentAddAgrodealerDao
import com.steve_md.smartmkulima.data.room.FieldAgentEarningsDao
import com.steve_md.smartmkulima.data.room.GAPDao
import com.steve_md.smartmkulima.data.room.LocalFarmCycleDao
import com.steve_md.smartmkulima.data.room.OrdersDao
import com.steve_md.smartmkulima.model.Cycle
import com.steve_md.smartmkulima.model.LocalFarmCycle
import com.steve_md.smartmkulima.model.NewFarmField
import com.steve_md.smartmkulima.model.OrderCheckoutByFarmer
import com.steve_md.smartmkulima.model.fieldagentmodels.FieldAgentAddAgroDealerData
import com.steve_md.smartmkulima.model.fieldagentmodels.FieldAgentEarnings
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceExpenseRecords
import com.steve_md.smartmkulima.model.financialdata.FarmFinanceRevenueRecords
import com.steve_md.smartmkulima.model.financialdata.FarmFinancialDataSummary
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

    private val farmCycleExpensesRecordsDao: FarmCycleExpensesRecordsDao = database.farmCycleExpensesRecordsDao()
    private val farmCycleRevenueRecordsDao: FarmCycleRevenueRecordsDao = database.farmCycleRevenueRecordsDao()

    private val farmSummaryRecordsDao: FarmSummaryRecordsDao = database.farmSummaryRecordsDao()

    // orders
    private val ordersDao : OrdersDao = database.ordersDao()

    private val fieldAgentAddAgrodealerDao: FieldAgentAddAgrodealerDao = database.fieldAgentAgrodealerDao()

    private val fieldAgentEarningsDao: FieldAgentEarningsDao = database.fieldAgentEarningsDao()

    suspend fun insertCycle(cycle: LocalFarmCycle) = apiRequestByResource {
        localCycleDao.insertLocalFarmCycle(cycle)
    }

    val allCycles : Flow<List<Cycle>> = gapDao.getAllCycle()


    fun getAllCycles(): LiveData<List<LocalFarmCycle>> {
        return localCycleDao.getAllFarmCycle()
    }

    suspend fun updateTaskStatus(status: String, cropName: String) {
        localCycleDao.updateTaskStatus(status, cropName)
    }

    suspend fun updateToNewCommentsCropCycle(comments: String, cropName: String) {
        localCycleDao.updateToNewComments(comments, cropName)
    }

    fun getAllFarmFields(): LiveData<List<NewFarmField>> {
        return farmFieldDao.getAllFarmFields()
    }

    suspend fun insertFarmField(farmField: NewFarmField) = apiRequestByResource {
        farmFieldDao.saveFarmField(farmField)
    }

    suspend fun insertNewCycleExpense(farmCycleExpenseRecords: FarmFinanceExpenseRecords) = apiRequestByResource {
        farmCycleExpensesRecordsDao.saveCycleExpense(farmCycleExpenseRecords)
    }

    suspend fun insertNewCycleRevenue(farmCycleRevenueRecords: FarmFinanceRevenueRecords) = apiRequestByResource {
        farmCycleRevenueRecordsDao.saveCycleRevenue(farmCycleRevenueRecords)
    }

    fun getAllCycleExpenses() : LiveData<List<FarmFinanceExpenseRecords>> {
        return farmCycleExpensesRecordsDao.getAllCycleExpenses()
    }

    fun getAllCycleRevenues() : LiveData<List<FarmFinanceRevenueRecords>> {
        return farmCycleRevenueRecordsDao.getAllCycleRevenues()
    }

    suspend fun deleteAllCycleExpenses() = apiRequestByResource {
        farmCycleExpensesRecordsDao.deleteCycleExpenses()
    }

    suspend fun deleteAllCycleRevenues() = apiRequestByResource {
        farmCycleRevenueRecordsDao.deleteCycleRevenues()
    }

    suspend fun saveFarmSummaryRecord(farmSumm: FarmFinancialDataSummary) =  apiRequestByResource {
        farmSummaryRecordsDao.saveSummaryRecord(farmSumm)
    }

    fun getAllSummaryRecords() : LiveData<List<FarmFinancialDataSummary>> {
        return farmSummaryRecordsDao.getAllFarmFinancialDataSummary()
    }

    suspend fun deleteAllSummaryRecords() = apiRequestByResource {
        farmSummaryRecordsDao.deleteAllSummary()
    }


    // pick the total expenses for the selected crop
    fun getTotalExpensesForCrop(cropName: String): LiveData<String?> {
        return farmCycleExpensesRecordsDao.getTotalExpensesForCrop(cropName)
    }

    // pick total sales made for the selected crop
    fun getTotalSalesForCrop(cropName: String): LiveData<String?> {
        return farmCycleRevenueRecordsDao.getTotalSalesAfterHarvestRevenues(cropName)
    }

    // orders
    suspend fun saveOrders(orderCheckoutByFarmer: OrderCheckoutByFarmer) =  apiRequestByResource {
        ordersDao.saveOrder(orderCheckoutByFarmer)
    }

    fun getSpecificOrdersForAgrodealerID(agrodealerId: String): LiveData<List<OrderCheckoutByFarmer>> {
        return ordersDao.getSpecificOrdersForAgroDealerID(agrodealerId)
    }

    fun getAllOrdersToTheFarmer(): LiveData<List<OrderCheckoutByFarmer>> {
        return ordersDao.fetchAllOrdersToTheFarmer()
    }

    suspend fun updateOrderStatus(newStatus: String, agrodealerId: String) {
        ordersDao.updateOrderStatus(newStatus, agrodealerId)
    }

    suspend fun fieldAgentAddANewAgroDealer(fieldAgentAddAgroDealerData: FieldAgentAddAgroDealerData) =  apiRequestByResource {
        fieldAgentAddAgrodealerDao.fieldAgentAddANewAgrodealer(fieldAgentAddAgroDealerData)
    }

    fun getAllFieldAgentAddedAgroDealers(): LiveData<List<FieldAgentAddAgroDealerData>> {
        return fieldAgentAddAgrodealerDao.getAllFieldAgentAddedAgrodealers()
    }

    // Field agent earnings METHODS
    suspend fun saveEarnings(fieldAgentEarnings: FieldAgentEarnings) =  apiRequestByResource {
        fieldAgentEarningsDao.saveFieldAgentEarnings(fieldAgentEarnings)
    }

    suspend fun updateFieldAgentEarnings(newPoints: Int, earnings: Double) {
        fieldAgentEarningsDao.updateFieldAgentEarnings(newPoints, earnings)
    }

    fun getAllFieldAgentEarnings(): LiveData<FieldAgentEarnings> {
        return fieldAgentEarningsDao.fetchAllFieldAgentEarnings()
    }
}