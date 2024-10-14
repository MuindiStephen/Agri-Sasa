package com.steve_md.smartmkulima.di

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.steve_md.smartmkulima.data.room.AppDatabase
import com.steve_md.smartmkulima.data.room.BuyersDao
import com.steve_md.smartmkulima.data.room.FarmProduceDao
import com.steve_md.smartmkulima.data.room.FieldAgentUserDao
import com.steve_md.smartmkulima.data.room.GAPDao
import com.steve_md.smartmkulima.model.BuyerCartDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Database module - di
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesRoomDatabase(context: Application): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "ShambaRoomDB.db")
            .allowMainThreadQueries()  // without blocking the main thread
            .fallbackToDestructiveMigration() //  Want database to not be cleared when upgrading versions from 1_2
            // .addMigrations()
            .build()
    }


    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE localcycle ADD COLUMN status TEXT NOT NULL DEFAULT 'Upcoming'")
        }
    }
    @Provides
    @Singleton
    fun providesFarmProduceDao(appDatabase: AppDatabase): FarmProduceDao {
        return appDatabase.farmProduceDao()
    }

    @Provides
    @Singleton
    fun providesCycleDao(appDatabase: AppDatabase): GAPDao {
        return appDatabase.gapDao()
    }

    @Singleton
    @Provides
    fun providesFieldAgentDao(appDatabase: AppDatabase): FieldAgentUserDao {
        return appDatabase.fieldAgentUserDao()
    }

    @Singleton
    @Provides
    fun providesBuyerDao(appDatabase: AppDatabase): BuyersDao {
        return appDatabase.buyerDao()
    }

    @Singleton
    @Provides
    fun providesBuyerCartDao(appDatabase: AppDatabase): BuyerCartDao {
        return appDatabase.buyerCartDao()
    }
}