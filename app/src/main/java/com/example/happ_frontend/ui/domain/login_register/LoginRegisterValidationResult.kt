package com.example.happ_frontend.ui.domain.login_register

import androidx.annotation.StringRes

sealed interface LoginRegisterValidationResult {
    data object Success : LoginRegisterValidationResult

    class Failure(
        @StringRes val errorResId: Int,
        vararg val formatArgs: Any
    ) : LoginRegisterValidationResult
}