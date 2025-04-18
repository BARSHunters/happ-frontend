package com.example.happ_frontend.model.activity.communication

import com.example.happ_frontend.model.activity.request.ActivityRequest
import com.example.happ_frontend.model.activity.response.ActivityResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Body
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface ActivityApiService {
    companion object {
        const val BASE_URL = "http://10.0.2.2:3000/"

    }

    @GET("getActivities")
    suspend fun getActivities(): Response<ActivityResponse>

    @POST("newActivity")
    suspend fun createActivity(@Body request: ActivityRequest): Response<Unit>
} 