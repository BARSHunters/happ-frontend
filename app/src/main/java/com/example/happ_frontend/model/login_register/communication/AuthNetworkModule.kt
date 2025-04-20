package com.example.happ_frontend.model.login_register.communication

import com.example.happ_frontend.model.common.ApiConfig
import com.example.happ_frontend.model.login_register.data.AuthSharedPreferencesProvider
import com.example.happ_frontend.model.login_register.serialization.LocalDateTimeAdapter
import com.example.happ_frontend.model.login_register.serialization.LocalDateAdapter
import com.example.happ_frontend.model.login_register.serialization.LocalTimeAdapter
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.concurrent.TimeUnit

object AuthNetworkModule {
    private const val DEFAULT_API_TIMEOUT_SECONDS = ApiConfig.DEFAULT_API_TIMEOUT_SECONDS

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(DEFAULT_API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()


    private val retrofit = Retrofit.Builder()
        .baseUrl(UserDataApiService.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val authApiService: AuthApiService by lazy { retrofit.create(AuthApiService::class.java) }
}