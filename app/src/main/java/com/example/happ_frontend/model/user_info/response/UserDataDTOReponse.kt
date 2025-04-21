package com.example.happ_frontend.model.user_info.response

import com.example.happ_frontend.model.user_info.request.UserDataDTO
import com.example.happ_frontend.model.user_info.request.WeightDesire
import java.time.LocalDate

data class UserDataDtoResponse(
    val username: String,
    val name: String,
    val birthDate: LocalDate,
    val gender: com.example.happ_frontend.model.user_info.request.Gender,
    val heightCm: Int,
    val weightKg: Float,
    val weightDesire: WeightDesire
) {
    companion object {
        @JvmStatic
        fun fromResponseDto(response: UserDataDTO): UserDataDtoResponse {
            return UserDataDtoResponse(
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