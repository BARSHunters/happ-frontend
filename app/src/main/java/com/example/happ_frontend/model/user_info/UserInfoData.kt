package com.example.happ_frontend.model.user_info;

import java.time.LocalDate

data class UserInfoData(
        val username: String = "",
        val name: String = "",
        val birthDate: LocalDate? = null,
        val gender: Gender = Gender.MALE,
        val heightCm: Int = 170,
        val weightKg: Float = 70f,
        val weightDesire: WeightDesire = WeightDesire.REMAIN,
        val friends: List<String> = emptyList(),
        val friendsCount: Int = 0
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
