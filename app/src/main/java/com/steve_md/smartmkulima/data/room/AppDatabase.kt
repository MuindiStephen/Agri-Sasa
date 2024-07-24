package com.steve_md.smartmkulima.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.steve_md.smartmkulima.data.room.converters.Converters
import com.steve_md.smartmkulima.data.room.converters.LocalFarmCycleConverter
import com.steve_md.smartmkulima.model.Cycle
import com.steve_md.smartmkulima.model.FarmProduce
import com.steve_md.smartmkulima.model.LocalFarmCycle
import com.steve_md.smartmkulima.model.Tasks
import com.steve_md.smartmkulima.model.Transaction


/**
 * Room database
 */
@TypeConverters(Converters::class,LocalFarmCycleConverter::class)
@Database(entities = [Transaction::class, FarmProduce::class, Cycle::class,
    Tasks::class, LocalFarmCycle::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun farmProduceDao() : FarmProduceDao

    abstract fun gapDao(): GAPDao

    abstract fun localFarmCycleDao(): LocalFarmCycleDao

    /**
     * Implement singleton pattern in room to prevent
     * multiple instances of room database opening at the sametime
     */

    /**
    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
    */
}