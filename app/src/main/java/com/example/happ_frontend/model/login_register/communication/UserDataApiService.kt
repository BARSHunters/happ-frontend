package com.example.happ_frontend.model.login_register.communication

import com.example.happ_frontend.model.common.ApiConfig
import com.example.happ_frontend.model.login_register.request.LoginDto
import com.example.happ_frontend.model.login_register.request.RegisterDto
import com.example.happ_frontend.model.login_register.response.LoginResponse
import com.example.happ_frontend.model.login_register.response.UserDataResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserDataApiService {
    companion object {
        const val BASE_URL = ApiConfig.DEFAULT_BASE_URL
    }

    @GET("getUserInfo")
    suspend fun getUserInfo(): Response<UserDataResponse>

    @POST("updateInfo")
    suspend fun updateInfo(): Response<Unit>
}