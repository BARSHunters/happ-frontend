package com.example.happ_frontend.model.search.communication

import com.example.happ_frontend.model.login_register.data.AuthSharedPreferencesProvider
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object SearchRetrofitInstance {
    private const val DEFAULT_API_TIMEOUT_SECONDS = 5L

    private const val BASE_URL = "http://10.0.2.2:3000/" //TODO исправить URL

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val jwt = AuthSharedPreferencesProvider.editor?.jwt
            val request = chain.request().newBuilder()
                .let {
                    if (jwt != null)
                        it.addHeader("Authorization", "Bearer $jwt")
                    else it
                }
                .build()
            chain.proceed(request)
        }
        .connectTimeout(DEFAULT_API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val searchApiService: SearchApiService by lazy {
        retrofit.create(SearchApiService::class.java)
    }
}