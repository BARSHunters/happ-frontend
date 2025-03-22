package com.example.happ_frontend.ui.domain.login_register

import androidx.annotation.StringRes

/**
 * Represents the result of a login or registration validation.
 *
 * This sealed interface has two possible implementations:
 * - [Success] - Indicates that the validation was successful.
 * - [Failure] - Indicates that the validation failed. Contains an error resource ID and optional format arguments.
 * @author Vad1mChK
 */
sealed interface LoginRegisterValidationResult {
    /**
     * Represents a successful validation result.
     * @author Vad1mChK
     */
    data object Success : LoginRegisterValidationResult

    /**
     * Represents a failed validation result.
     *
     * @param errorResId The resource ID of the error message.
     * @param formatArgs Optional format arguments for the error message.
     * @author Vad1mChK
     */
    class Failure(
        @StringRes val errorResId: Int,
        vararg val formatArgs: Any
    ) : LoginRegisterValidationResult
}