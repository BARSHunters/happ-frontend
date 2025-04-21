package com.example.happ_frontend.model.user_info.communication

import com.example.happ_frontend.model.common.ApiConfig
import com.example.happ_frontend.model.user_info.request.FriendsListResponse
import com.example.happ_frontend.model.user_info.request.UserDataDTO
import com.example.happ_frontend.model.user_info.response.UserDataDtoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserInfoApiService {
    companion object {
        const val BASE_URL = ApiConfig.DEFAULT_BASE_URL
    }

    @GET("getUserInfo")
    suspend fun getUserInfo(): Response<UserDataDTO>

    @GET("getFriends")
    suspend fun getFriends(): Response<FriendsListResponse>

    @POST("updateInfo")
    suspend fun updateInfo(@Body request: UserDataDtoResponse): Response<Unit>
}
