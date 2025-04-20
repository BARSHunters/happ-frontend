package com.example.happ_frontend.model.user_info.data

interface UserInfoRepository {
    suspend fun getUserInfo(): UserInfoEntity
    suspend fun saveUserInfo(userInfo: UserInfoEntity)
    fun clearJwt()
}