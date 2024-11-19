package com.steve_md.smartmkulima.di

import com.steve_md.smartmkulima.data.remote.FarmProduceApiService
import com.steve_md.smartmkulima.data.remote.RetrofitApiService
import com.steve_md.smartmkulima.data.remote.UbiBotIoTWebService
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
import java.util.concurrent.TimeUnit
import javax.inject.Named
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

            // Allow more time for the server to respond
            .connectTimeout(30, TimeUnit.SECONDS)  // Connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Read timeout
            .writeTimeout(30, TimeUnit.SECONDS)   // Write timeout
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
    @Named("retrofit1")
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
    @Named("retrofit2")
    fun providesRetrofit2(
        okHttpClient: OkHttpClient,
        converter: Converter.Factory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://script.googleusercontent.com/")
            .client(okHttpClient)
            .addConverterFactory(converter)
            .build()
    }

    @Provides
    @Singleton
    fun providesApiService(@Named("retrofit1") retrofit: Retrofit): FarmProduceApiService {
        return retrofit.create(FarmProduceApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesRetrofitService(@Named("retrofit1") retrofit: Retrofit): RetrofitApiService {
        return retrofit.create(RetrofitApiService::class.java)
    }

    @Singleton
    @Provides
    fun providesUbiBotIoTWebService(@Named("retrofit2") retrofit2: Retrofit): UbiBotIoTWebService {
        return retrofit2.create(UbiBotIoTWebService::class.java)
    }

}
