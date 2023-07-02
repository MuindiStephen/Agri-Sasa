package com.steve_md.smartmkulima.di

import com.steve_md.smartmkulima.data.remote.FarmProduceApiService
import com.steve_md.smartmkulima.data.repositories.FarmProduceRepository
import com.steve_md.smartmkulima.data.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


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
}