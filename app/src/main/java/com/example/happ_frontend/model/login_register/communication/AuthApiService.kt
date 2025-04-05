package com.example.happ_frontend.model.login_register.communication

import com.example.happ_frontend.model.login_register.request.LoginDto
import com.example.happ_frontend.model.login_register.request.RegisterDto
import com.example.happ_frontend.model.login_register.response.JwtValidationResponse
import com.example.happ_frontend.model.login_register.response.LoginResponse
import com.example.happ_frontend.model.login_register.response.UserDataResponse
import com.example.happ_frontend.ui.navigation.RegisterDest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApiService {
    companion object {
        const val BASE_URL = "http://10.0.2.2:3000/api/auth/" // TODO replace with actual API URL and endpoints
    }

    @POST("register")
    suspend fun register(
        @Body request: RegisterDto
    ): Response<LoginResponse>

    @POST("login")
    suspend fun login(
        @Body request: LoginDto
    ): Response<LoginResponse>

    @GET("checkJwt")
    suspend fun checkJwt(): Response<JwtValidationResponse>
}