package com.example.happ_frontend.model.user_info.request

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

enum class Gender {
    MALE,
    FEMALE
}

enum class WeightDesire {
    LOSS,
    REMAIN,
    GAIN
}
