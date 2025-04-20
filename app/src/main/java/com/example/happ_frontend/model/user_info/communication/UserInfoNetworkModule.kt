package com.example.happ_frontend.model.user_info.communication

import com.example.happ_frontend.model.login_register.data.AuthSharedPreferencesProvider
import com.example.happ_frontend.model.login_register.serialization.LocalDateAdapter
import com.example.happ_frontend.model.login_register.serialization.LocalDateTimeAdapter
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

object UserInfoNetworkModule {
    private const val TIMEOUT_SECONDS = 5L

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor { chain ->
            val jwt = AuthSharedPreferencesProvider.editor?.jwt
            val request = chain.request().newBuilder().apply {
                if (!jwt.isNullOrBlank()) {
                    addHeader("Authorization", "Bearer $jwt")
                }
            }.build()
            chain.proceed(request)
        }
        .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
        .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(UserInfoApiService.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val userInfoApiService: UserInfoApiService by lazy {
        retrofit.create(UserInfoApiService::class.java)
    }
}
