package com.example.happ_frontend.ui.domain.login_register

import com.example.happ_frontend.model.login_register.Gender
import com.example.happ_frontend.model.login_register.WeightDesire
import kotlinx.datetime.LocalDate

/**
 * Data class representing form data for login and registration screens.
 *
 * @property username The username entered by the user.
 * @property password The password entered by the user.
 * @property confirmPassword The password confirmation entered by the user.
 * @property passwordCensored Indicates whether the password should be censored.
 * @property name The name entered by the user. Default is an empty string.
 * @property birthDate The birth date entered by the user.
 * @property gender The gender selected by the user.
 * @property heightCm The height entered by the user in centimeters.
 * @property weightKg The weight entered by the user in kilograms.
 * @property weightDesire The weight desire selected by the user.
 * @property errorMessage The error message to be displayed.
 *
 * @see Gender
 * @see WeightDesire
 * @author Vad1mChK
 */
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