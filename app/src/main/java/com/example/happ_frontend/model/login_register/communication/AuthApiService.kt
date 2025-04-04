package com.example.happ_frontend.model.login_register.communication

import com.example.happ_frontend.model.login_register.request.LoginDto
import com.example.happ_frontend.model.login_register.request.RegisterDto
import com.example.happ_frontend.model.login_register.response.LoginResponse
import com.example.happ_frontend.ui.navigation.RegisterDest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    companion object {
        const val BASE_URL = "https://happ-backend.onrender.com/api/auth/"
    }

    @POST("/register")
    suspend fun register(
        @Body request: RegisterDto
    ): Response<LoginResponse>

//    @POST("/checkUsername") // TODO maybe change endpoint name
//    suspend fun checkUsernameAvailability(
//        @Body request: RegisterCheckUsernameDto
//    ): Boolean

    @POST("/login")
    suspend fun login(
        @Body request: LoginDto
    ): Response<LoginResponse>
}