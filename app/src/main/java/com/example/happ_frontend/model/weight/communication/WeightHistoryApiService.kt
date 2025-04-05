package com.example.happ_frontend.model.weight.communication

import com.example.happ_frontend.model.weight.request.APIGatewayToWeightHistoryRequest
import com.example.happ_frontend.model.weight.response.WeightHistoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

interface WeightHistoryApiService {
    companion object {
        const val BASE_URL = "http://10.0.2.2:3000/api/weight/" // TODO replace with actual API URL and endpoints
    }

    @GET("weightHistoryAndPredictions")
    suspend fun getWeightHistoryAndPredictions(
        @Body request: APIGatewayToWeightHistoryRequest
    ): Response<WeightHistoryResponse>
}