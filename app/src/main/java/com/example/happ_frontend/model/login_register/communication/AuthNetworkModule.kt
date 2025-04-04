package com.example.happ_frontend.model.login_register.communication

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object AuthNetworkModule {
    private const val DEFAULT_API_TIMEOUT_SECONDS = 5L

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(DEFAULT_API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(AuthApiService.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApiService: AuthApiService = retrofit.create(AuthApiService::class.java)
}