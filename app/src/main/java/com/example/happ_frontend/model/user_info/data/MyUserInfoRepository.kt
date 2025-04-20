package com.example.happ_frontend.model.user_info.data

import android.util.Log
import com.example.happ_frontend.model.login_register.data.AuthSharedPreferencesProvider
import com.example.happ_frontend.model.user_info.communication.UserInfoApiService
import com.example.happ_frontend.model.user_info.request.UserInfoDAO

class MyUserInfoRepository(
    private val api: UserInfoApiService,
    private val dao: UserInfoDAO
) : UserInfoRepository {

    init {
        Log.d("MyUserInfoRepository", "MyUserInfoRepository initialized")
    }

    override suspend fun getUserInfo(): UserInfoEntity {
        val local = dao.getUserInfo()
        if (local != null) return local

        val userResponse = api.getUserInfo()
        val friendsResponse = api.getFriends()

        if (userResponse.isSuccessful && friendsResponse.isSuccessful) {
            val userDto = userResponse.body()!!
            val friendsDto = friendsResponse.body()!!
            val entity = UserInfoConverter.fromDto(userDto, friendsDto)

            dao.insertOrUpdate(entity)
            return entity
        } else {
            throw Exception("Failed to fetch user info or friends")
        }
    }

    override suspend fun saveUserInfo(userInfo: UserInfoEntity) {
        val dto = UserInfoConverter.toDto(userInfo)
        val response = api.updateInfo(dto)

        if (!response.isSuccessful) {
            throw Exception("Failed to save user info to server")
        }

        dao.insertOrUpdate(userInfo)
    }

    override fun clearJwt() {
        AuthSharedPreferencesProvider.editor?.jwt = null
    }
}
