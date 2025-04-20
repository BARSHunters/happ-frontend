package com.example.happ_frontend.model.nutrition.communication

import com.example.happ_frontend.model.nutrition.request.NutritionRequest
import com.example.happ_frontend.model.nutrition.response.NutritionResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Query

interface NutritionApiService {
    companion object {
        const val BASE_URL = "http://10.0.2.2:3000/"
    }

    @GET("getMeals")
    suspend fun getMeals(@Header("Authorization") token: String): Response<NutritionResponse>

    @GET("getNutritionsByWeek")
    suspend fun getNutritionsByWeek(
        @Header("Authorization") token: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<NutritionResponse>

    @POST("generateNutrition")
    suspend fun generateNutrition(
        @Header("Authorization") token: String,
        @Body request: NutritionRequest
    ): Response<NutritionResponse>
} 