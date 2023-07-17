package com.steve_md.smartmkulima.data.remote

import android.os.Build
import com.steve_md.smartmkulima.utils.Constants.BASE_URL
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.net.SocketFactory


/**
 * Retrofit Api client.
 * Sending data through Unsecure Http
 */
object ApiClient {

   // private val protocols: List<Protocol> = listOf(Protocol.HTTP_1_1, Protocol.HTTP_2)

    private var mHttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private var mOkHttpClient = OkHttpClient.Builder()
        .addInterceptor(mHttpLoggingInterceptor)
        .apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val connectionSpec = ConnectionSpec.Builder(ConnectionSpec.COMPATIBLE_TLS)
                    .build()
                connectionSpecs(listOf(connectionSpec, ConnectionSpec.CLEARTEXT))
            } else {
                connectionSpecs(listOf(ConnectionSpec.CLEARTEXT))
            }
        }
            .connectTimeout(220,TimeUnit.SECONDS)
            .readTimeout(220,TimeUnit.SECONDS)
            .writeTimeout(120,TimeUnit.SECONDS)
        .socketFactory(socketFactory = SocketFactory.getDefault())
        .build()


    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(mOkHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}


