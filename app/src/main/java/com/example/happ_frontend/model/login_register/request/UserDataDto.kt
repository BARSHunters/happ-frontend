package com.example.happ_frontend.model.login_register.request

import com.example.happ_frontend.model.login_register.Gender
import com.example.happ_frontend.model.login_register.WeightDesire
import com.example.happ_frontend.model.login_register.response.UserDataResponse
import java.time.LocalDate

data class UserDataDto(
    val username: String,
    val name: String,
    val birthDate: LocalDate,
    val gender: Gender,
    val heightCm: Int,
    val weightKg: Float,
    val weightDesire: WeightDesire
) {
    companion object {
        @JvmStatic
        fun fromResponseDto(response: UserDataResponse): UserDataDto {
            return UserDataDto(
                username = response.username,
                name = response.name,
                birthDate = response.birthDate,
                gender = response.gender,
                heightCm = response.height,
                weightKg = response.weight,
                weightDesire = response.weightDesire
            )
        }
    }
}