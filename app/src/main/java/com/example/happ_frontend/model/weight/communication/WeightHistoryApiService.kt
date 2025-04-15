package com.example.happ_frontend.model.weight.communication

import com.example.happ_frontend.model.login_register.request.UserDataDto
import com.example.happ_frontend.model.login_register.response.UserDataResponse
import com.example.happ_frontend.model.weight.response.WeightHistoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WeightHistoryApiService {
    companion object {
        const val BASE_URL = "http://10.0.2.2:3000/" // TODO replace with actual API URL and endpoints
    }

    @GET("getWeightHistory")
    suspend fun getWeightHistory(
//        @Body request: APIGatewayToWeightHistoryRequest
    ): Response<WeightHistoryResponse>

    @GET("getUserInfo")
    suspend fun getUserInfo(): Response<UserDataResponse>

    @POST("updateInfo")
    suspend fun updateInfo(@Body request: UserDataDto): Response<Unit>
}