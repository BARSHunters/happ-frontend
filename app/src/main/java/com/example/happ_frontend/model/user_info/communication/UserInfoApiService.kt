package com.example.happ_frontend.model.user_info.communication

import com.example.happ_frontend.model.user_info.request.FriendsListResponse
import com.example.happ_frontend.model.user_info.request.UserDataDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserInfoApiService {
    companion object {
        const val BASE_URL = "http://10.0.2.2:3000/" // Заменишь при деплое
    }

    @GET("getUserInfo")
    suspend fun getUserInfo(): Response<UserDataDTO>

    @GET("getFriendsList")
    suspend fun getFriends(): Response<FriendsListResponse>

    @POST("updateInfo")
    suspend fun updateInfo(@Body request: UserDataDTO): Response<Unit>
}
