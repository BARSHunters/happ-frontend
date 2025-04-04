package com.example.happ_frontend.ui.domain.login_register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.happ_frontend.model.login_register.Gender
import com.example.happ_frontend.model.login_register.WeightDesire
import com.example.happ_frontend.model.login_register.communication.AuthApiService
import com.example.happ_frontend.model.login_register.communication.AuthNetworkModule
import com.example.happ_frontend.model.login_register.data.AuthSharedPreferencesEditor
import com.example.happ_frontend.model.login_register.data.AuthSharedPreferencesProvider
import com.example.happ_frontend.model.login_register.request.LoginDto
import com.example.happ_frontend.model.login_register.request.RegisterDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * A ViewModel for managing login and registration data.
 * It uses Kotlin Flow to handle state changes and provides methods for validating and accepting user input.
 * @author Vad1mChK
 */
class AuthViewModel (
    private val authApi: AuthApiService = AuthNetworkModule.authApiService,
    private val prefs: AuthSharedPreferencesEditor = AuthSharedPreferencesProvider.editor
        ?: throw IllegalStateException("AuthSharedPreferencesEditor not initialized yet")
) : ViewModel() {
    private val data = MutableStateFlow(AuthFormData())
    val uiState: StateFlow<AuthFormData> = data.asStateFlow()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

//    init {
//        Log.d("AuthViewModel#init", "ViewModel initialized")
//    }

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

    var passedFirstRegistrationPage: Boolean
        get() = data.value.passedFirstRegistrationPage
        set(value) {
            data.update { login -> login.copy(passedFirstRegistrationPage = value) }
        }

    /**
     * Validates the login credentials.
     *
     * @return true if both username and password are valid, false otherwise.
     * @author Vad1mChK
     */
    fun validateLogin(): Boolean {
        return listOf(
            AuthValidator.UsernameValidator().validate(username),
            AuthValidator.PasswordValidator().validate(password)
        ).all { it is AuthValidationResult.Success }
    }

    /**
     * Validates the first part of the registration form.
     *
     * @return true if both username, password, and confirm password are valid, false otherwise.
     * @author Vad1mChK
     */
    fun validateRegisterFirstPart(): Boolean {
        return listOf(
            AuthValidator.UsernameValidator().validate(username),
            AuthValidator.PasswordValidator().validate(password),
            AuthValidator.PasswordMatchValidator(password).validate(confirmPassword)
        ).all {
            it is AuthValidationResult.Success
        }
    }

    /**
     * Validates the entire registration form.
     *
     * @return true if all fields in the registration form are valid, false otherwise.
     */
    fun validateRegister(): Boolean {
        val firstPartResult = validateRegisterFirstPart()
        return firstPartResult && listOf(
            AuthValidator.NameValidator().validate(name),
            AuthValidator.IntInRangeValidator(
                min = AuthFormData.MIN_HEIGHT_CM,
                max = AuthFormData.MAX_HEIGHT_CM,
            ).validate(heightCm),
            AuthValidator.FloatInRangeValidator(
                min = AuthFormData.MIN_WEIGHT_KG,
                max = AuthFormData.MAX_WEIGHT_KG,
            ).validate(weightKg),
        ).all { it is AuthValidationResult.Success }
    }

    fun registerUser() {
        if (!validateRegister()) return

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = authApi.register(
                    RegisterDto(
                        username = data.value.username,
                        password = data.value.password,
                        name = data.value.name,
                        birthDate = data.value.birthDate,
                        gender = data.value.gender,
                        heightCm = data.value.heightCm,
                        weightKg = data.value.weightKg,
                        weightDesire = data.value.weightDesire
                    )
                )

                if (response.isSuccessful) {
                    response.body()?.let { authResponse ->
                        prefs.username = data.value.username
                        prefs.jwt = authResponse.jwt ?: ""
                        _authState.value = AuthState.Success(authResponse.jwt ?: "")
                    }
                } else {
                    _authState.value = AuthState.Error("Server error: ${response.code()}")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Network error: ${e.localizedMessage}")
            }
        }
    }

    fun loginUser() {
        if (!validateLogin()) return

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val response = authApi.login(
                    LoginDto(
                        username = data.value.username,
                        password = data.value.password
                    )
                )

                if (response.isSuccessful) {
                    response.body()?.let { authResponse ->
                        prefs.username = data.value.username
                        prefs.jwt = authResponse.jwt ?: ""
                        _authState.value = AuthState.Success(authResponse.jwt ?: "")
                    }
                } else {
                    _authState.value = AuthState.Error("Server error: ${response.code()}")
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Network error: ${e.localizedMessage}")
            }
        }
    }

    fun clearAuthState() {
        _authState.value = AuthState.Idle
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val jwt: String) : AuthState()
    data class Error(val message: String) : AuthState()
}