package com.example.happ_frontend.model.login_register.response

import com.example.happ_frontend.model.login_register.Gender
import com.example.happ_frontend.model.login_register.WeightDesire
import java.time.LocalDate

data class UserDataResponse(
    val username: String,
    val name: String,
    val age: Int,
    val birthDate: LocalDate,
    val gender: Gender,
    val height: Int,
    val weight: Float,
    val weightDesire: WeightDesire
)