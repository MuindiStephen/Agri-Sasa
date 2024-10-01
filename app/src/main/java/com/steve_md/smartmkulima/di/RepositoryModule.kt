package com.steve_md.smartmkulima.di

import com.steve_md.smartmkulima.data.remote.FarmProduceApiService
import com.steve_md.smartmkulima.data.repositories.AuthRepository
import com.steve_md.smartmkulima.data.repositories.FarmCycleRepository
import com.steve_md.smartmkulima.data.repositories.FarmProduceRepository
import com.steve_md.smartmkulima.data.repositories.impl.AuthRepositoryImpl
import com.steve_md.smartmkulima.data.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton


/**
 * Repository module - di
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesRepository(
        farmProduceApiService: FarmProduceApiService,
        appDatabase: AppDatabase
    ): FarmProduceRepository {
        return FarmProduceRepository(farmProduceApiService, appDatabase)
    }

    @Singleton
    @Provides
    fun providesFarmCycleRepo(
        database: AppDatabase
    ): FarmCycleRepository {
        return FarmCycleRepository(database)
    }

    @Singleton
    @Provides
    fun providesDispatcher() = Dispatchers.Main as CoroutineDispatcher


    @Singleton
    @Provides
    fun providesAuthRepository(): AuthRepository {
        return AuthRepositoryImpl()
    }



}