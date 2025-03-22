package com.example.happ_frontend.ui.domain.login_register

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.happ_frontend.model.login_register.Gender
import com.example.happ_frontend.model.login_register.WeightDesire
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate

class LoginRegisterViewModel : ViewModel() {
    private val data = MutableStateFlow(LoginRegisterFormData())
    val uiState: StateFlow<LoginRegisterFormData> = data.asStateFlow()

    init {
        Log.d("LoginViewModel#<init>", "LoginViewModel initialized")
    }

    var username: String
        get() = data.value.username
        set(value) {
            data.update { login -> login.copy(username = value) }
        }

    var password: String
        get() = data.value.password
        set(value) {
            data.update { login -> login.copy(password = value) }
        }

    var confirmPassword: String
        get() = data.value.confirmPassword
        set(value) {
            data.update { login -> login.copy(confirmPassword = value) }
        }

    var passwordCensored: Boolean
        get() = data.value.passwordCensored
        set(value) {
            data.update { login -> login.copy(passwordCensored = value) }
        }

    var name: String
        get() = data.value.name
        set(value) {
            data.update { login -> login.copy(name = value) }
        }

    var birthDate: LocalDate
        get() = data.value.birthDate
        set(value) {
            data.update { login -> login.copy(birthDate = value) }
        }

    var gender: Gender
        get() = data.value.gender
        set(value) {
            data.update { login -> login.copy(gender = value) }
        }

    var heightCm: Int
        get() = data.value.heightCm
        set(value) {
            data.update { login -> login.copy(heightCm = value) }
        }

    var weightKg: Float
        get() = data.value.weightKg
        set(value) {
            data.update { login -> login.copy(weightKg = value) }
        }

    var weightDesire: WeightDesire
        get() = data.value.weightDesire
        set(value) {
            data.update { login -> login.copy(weightDesire = value) }
        }

    fun validateLogin(): Boolean {
        return listOf(
            LoginRegisterValidator.UsernameValidator().validate(username),
            LoginRegisterValidator.PasswordValidator().validate(password)
        ).all { it is LoginRegisterValidationResult.Success }
    }

    fun validateRegisterFirstPart(): Boolean {
        return listOf(
            LoginRegisterValidator.UsernameValidator().validate(username),
            LoginRegisterValidator.PasswordValidator().validate(password),
            LoginRegisterValidator.PasswordMatchValidator(password).validate(confirmPassword)
        ).all { it is LoginRegisterValidationResult.Success }
    }

    fun validateRegister(): Boolean {
        val firstPartResult = validateRegisterFirstPart()
        return firstPartResult && listOf(
            LoginRegisterValidator.NameValidator().validate(name),
            LoginRegisterValidator.IntInRangeValidator(
                min = LoginRegisterFormData.MIN_HEIGHT_CM,
                max = LoginRegisterFormData.MAX_HEIGHT_CM,
            ).validate(heightCm),
            LoginRegisterValidator.FloatInRangeValidator(
                min = LoginRegisterFormData.MIN_WEIGHT_KG,
                max = LoginRegisterFormData.MAX_WEIGHT_KG,
            ).validate(weightKg),
        ).all { it is LoginRegisterValidationResult.Success }
    }

    fun acceptLogin() {
        Log.d("LoginViewModel#acceptLogin", "Accept ${data.value}")
    }

    fun acceptRegisterCheckUsername() {
        Log.d("LoginViewModel#acceptRegisterCheckUsername", "Accept ${data.value}")
    }

    fun acceptRegister() {
        Log.d("LoginViewModel#acceptRegisterCheckUsername", "Accept ${data.value}")
    }
}