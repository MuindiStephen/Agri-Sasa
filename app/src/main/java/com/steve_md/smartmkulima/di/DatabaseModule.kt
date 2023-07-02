package com.steve_md.smartmkulima.di

import android.app.Application
import androidx.room.Room
import com.steve_md.smartmkulima.data.room.AppDatabase
import com.steve_md.smartmkulima.data.room.FarmProduceDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


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

    @Provides
    @Singleton
    fun providesFarmProduceDao(appDatabase: AppDatabase): FarmProduceDao {
        return appDatabase.farmProduceDao()
    }
}