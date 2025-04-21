package com.example.happ_frontend.model.activity.communication

import com.example.happ_frontend.model.activity.response.ActivityDTO
import com.example.happ_frontend.model.activity.response.TrainingData
import retrofit2.Response
import retrofit2.http.*

interface ActivityApiService {
    companion object {
        const val BASE_URL = "http://10.0.2.2:3000/"
    }

    @POST("newActivity")
    suspend fun addActivity(
        @Header("Authorization") token: String,
        @Body activity: ActivityDTO
    ): Response<Unit>

    @GET("getActivitiesByWeek")
    suspend fun getActivities(
        @Header("Authorization") token: String,
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String
    ): Response<List<TrainingData>>
} 