package com.example.happ_frontend.model.login_register.request

import com.example.happ_frontend.model.login_register.Gender
import com.example.happ_frontend.model.login_register.WeightDesire
import java.time.LocalDate

/**
 * Data transfer object for user registration.
 *
 * @property username The unique identifier for the user in the system.
 * @property password The user's password for authentication.
 * @property name The user's full name.
 * @property birthDate The user's date of birth as a LocalDate.
 * @property gender The user's gender, represented by the [Gender] enum.
 * @property heightCm The user's height in centimeters.
 * @property weightKg The user's current weight in kilograms.
 * @property weightDesire The user's initial weight goal, represented by the [WeightDesire] enum.
 *
 * @author Vad1mChK
 */
data class RegisterDto(
    val username: String,
    val password: String,
    val name: String,
    val birthDate: LocalDate,
    val gender: Gender,
    val heightCm: Int,
    val weightKg: Float,
    val weightDesire: WeightDesire
)
