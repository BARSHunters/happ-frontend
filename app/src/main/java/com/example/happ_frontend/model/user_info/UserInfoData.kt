package com.example.happ_frontend.ui.domain.user_info;

data class UserInfoData (
        val name: String = "",
        val gender: String = "",
        val height: Float = 170f,
        val currentWeight: Float = 70f,
        val goalWeight: Float = 65f,
        val avatarUrl: String? = null, // Пока можно null
        val friends: List<Friend> = emptyList(),
        val friendRequests: List<Friend> = emptyList(),
        val achievements: List<String> = emptyList() // Можно заменить на модель Achievements
)

data class Friend(
        val name: String,
        val avatarUrl: String? = null
)
