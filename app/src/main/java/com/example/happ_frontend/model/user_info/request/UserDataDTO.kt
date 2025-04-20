package com.example.happ_frontend.model.user_info.request

import com.example.happ_frontend.model.user_info.Gender
import com.example.happ_frontend.model.user_info.WeightDesire
import java.time.LocalDate

data class UserDataDTO(
    val username: String,
    val name: String,
    val birthDate: LocalDate,
    val gender: Gender,
    val heightCm: Int,
    val weightKg: Float,
    val weightDesire: WeightDesire
)

data class FriendsListResponse(
    val friends: List<String>,
    val friendsCount: Int
)
