package com.steve_md.smartmkulima.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.steve_md.smartmkulima.model.FarmProduce
import com.steve_md.smartmkulima.model.Transaction


@Database(entities = [Transaction::class,FarmProduce::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun farmProduceDao() : FarmProduceDao

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