package com.steve_md.smartmkulima.di

import com.androidstudy.daraja.network.ApiClient
import com.steve_md.smartmkulima.data.remote.DarajaApiClient
import com.steve_md.smartmkulima.data.remote.FarmProduceApiService
import com.steve_md.smartmkulima.utils.Constants

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Network module - di
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun providesOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun providesHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun providesConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        converter: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constants.getStringBaseUrlDevelopment())
            .client(okHttpClient)
            .addConverterFactory(converter)
            .build()
    }

    @Provides
    @Singleton
    fun providesApiService(retrofit: Retrofit): FarmProduceApiService {
        return retrofit.create(FarmProduceApiService::class.java)
    }

//    @Singleton
//    @Provides
//    fun providesDarajaApiClient(): DarajaApiClient {
//        return DarajaApiClient(
//            consumerKey = "NgGUJ2LGJlVvjdLu8P7yDGIs6v4RmMF1114mYRUVTVOjsCii",
//            consumerSecret = "L5Ur0sgEuGfBAB7u8ynlH0bzerD1VABt7ABASfBAozvATLKETNHGcpTieX1vzLyv",
//            environment = Constants.SANDBOX_BASE_URL
//        )
//    }

}
