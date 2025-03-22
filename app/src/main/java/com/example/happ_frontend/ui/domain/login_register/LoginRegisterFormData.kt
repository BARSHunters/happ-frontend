package com.example.happ_frontend.ui.domain.login_register

import com.example.happ_frontend.model.login_register.Gender
import com.example.happ_frontend.model.login_register.WeightDesire
import kotlinx.datetime.LocalDate

data class LoginRegisterFormData(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val passwordCensored: Boolean = true,
    val name: String = "",
    val birthDate: LocalDate = LocalDate.now(),
    val gender: Gender = Gender.MALE,
    val heightCm: Int = DEFAULT_HEIGHT_CM,
    val weightKg: Float = DEFAULT_WEIGHT_KG,
    val weightDesire: WeightDesire = WeightDesire.REMAIN,
    val errorMessage: String? = null
) {
    companion object {
        const val DEFAULT_HEIGHT_CM = 175
        const val DEFAULT_WEIGHT_KG = 70.0f

        const val MIN_HEIGHT_CM = 50
        const val MAX_HEIGHT_CM = 250
        const val MIN_WEIGHT_KG = 40.0f
        const val MAX_WEIGHT_KG = 200.0f
    }
}