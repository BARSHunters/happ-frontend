package com.example.happ_frontend.model.nutrition.communication

import com.google.gson.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

object NutritionNetworkModule {
    private const val DEFAULT_API_TIMEOUT_SECONDS = 5L

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val localDateAdapter = object : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
        override fun serialize(
            src: LocalDate?,
            typeOfSrc: Type?,
            context: JsonSerializationContext?
        ): JsonElement {
            return JsonPrimitive(src?.format(DateTimeFormatter.ISO_DATE) ?: "")
        }

        override fun deserialize(
            json: JsonElement?,
            typeOfT: Type?,
            context: JsonDeserializationContext?
        ): LocalDate {
            return LocalDate.parse(json?.asString ?: "", DateTimeFormatter.ISO_DATE)
        }
    }

    private val gson = GsonBuilder()
        .registerTypeAdapter(LocalDate::class.java, localDateAdapter)
        .create()

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(DEFAULT_API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_API_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(NutritionApiService.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    val apiService: NutritionApiService = retrofit.create(NutritionApiService::class.java)
} 