package com.example.happ_frontend.ui.domain.login_register

import com.example.happ_frontend.R
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * A sealed interface for validating login and registration input fields.
 *
 * @param T The type of the value to be validated.
 * @author Vad1mChK
 */
sealed interface AuthValidator<T> {
    /**
     * Validates the given value according to the specific validator's rules.
     *
     * @param value The value to be validated.
     * @return A [AuthValidationResult] indicating the success or failure of the validation.
     * @author Vad1mChK
     */
    fun validate(value: T): AuthValidationResult

    /**
     * Validates if an integer is within a specified range.
     *
     * @property min The minimum allowed value (inclusive), or null if there's no lower bound.
     * @property max The maximum allowed value (inclusive), or null if there's no upper bound.
     * @author Vad1mChK
     */
    class IntInRangeValidator(
        val min: Int? = null, val max: Int? = null
    ) : AuthValidator<Int> {
        override fun validate(value: Int): AuthValidationResult {
            if (min != null && value < min) {
                return AuthValidationResult.Failure(R.string.auth_valid_error_number_min)
            }
            if (max != null && value > max) {
                return AuthValidationResult.Failure(R.string.auth_valid_error_number_max)
            }
            return AuthValidationResult.Success
        }
    }

    /**
     * Validates if a float is within a specified range.
     *
     * @property min The minimum allowed value (inclusive), or null if there's no lower bound.
     * @property max The maximum allowed value (inclusive), or null if there's no upper bound.
     * @author Vad1mChK
     */
    class FloatInRangeValidator(
        val min: Float? = null, val max: Float? = null
    ) : AuthValidator<Float> {
        override fun validate(value: Float): AuthValidationResult {
            if (min != null && value < min) {
                return AuthValidationResult.Failure(R.string.auth_valid_error_number_min)
            }
            if (max != null && value > max) {
                return AuthValidationResult.Failure(R.string.auth_valid_error_number_max)
            }
            return AuthValidationResult.Success
        }
    }

    /**
     * Validates a username string based on length and regex pattern.
     * @author Vad1mChK
     */
    class UsernameValidator : AuthValidator<String> {
        companion object {
            private const val USERNAME_REGEX_STRING = "^[a-zA-Z_][a-zA-Z0-9_]*\$"
            private const val USERNAME_MIN_LENGTH = 5
        }

        private val regex = Regex(USERNAME_REGEX_STRING)

        override fun validate(value: String): AuthValidationResult {
            if (value.length < USERNAME_MIN_LENGTH) {
                return AuthValidationResult.Failure(
                    R.string.auth_valid_error_username_length,
                    USERNAME_MIN_LENGTH
                )
            }
            if (!regex.matches(value)) {
                return AuthValidationResult.Failure(
                    R.string.auth_valid_error_username_regex
                )
            }
            return AuthValidationResult.Success
        }
    }

    /**
     * Validates a password string based on minimum length.
     * @author Vad1mChK
     */
    class PasswordValidator : AuthValidator<String> {
        companion object {
            private const val PASSWORD_MIN_LENGTH = 8
        }

        override fun validate(value: String): AuthValidationResult {
            if (value.length < PASSWORD_MIN_LENGTH) {
                return AuthValidationResult.Failure(
                    R.string.auth_valid_error_password_length,
                    PASSWORD_MIN_LENGTH
                )
            }
            return AuthValidationResult.Success
        }
    }

    /**
     * Validates if a password matches a previously entered password.
     *
     * @property firstPassword The password to match against.
     * @author Vad1mChK
     */
    class PasswordMatchValidator(val firstPassword: String) : AuthValidator<String> {
        override fun validate(value: String): AuthValidationResult {
            if (value != firstPassword) {
                return AuthValidationResult.Failure(
                    R.string.auth_valid_error_password_match
                )
            }
            return AuthValidationResult.Success
        }
    }

    /**
     * Validates a name string based on non-emptiness and regex pattern.
     * @author Vad1mChK
     */
    class NameValidator : AuthValidator<String> {
        companion object {
            private const val NAME_REGEX_STRING =
                "^\\p{L}(?:[-']?\\p{L}+)*(?: +\\p{L}(?:[-']?\\p{L}+)*)*\$"
        }

        private val regex = Regex(NAME_REGEX_STRING)

        override fun validate(value: String): AuthValidationResult {
            if (value.isEmpty()) {
                return AuthValidationResult.Failure(
                    R.string.auth_valid_error_name_notempty
                )
            }
            if (regex.matchEntire(value) == null) {
                return AuthValidationResult.Failure(
                    R.string.auth_valid_error_name_regex
                )
            }
            return AuthValidationResult.Success
        }
    }

    /**
     * Validates a number input string based on range and precision.
     *
     * @property min The minimum allowed value (inclusive), or null if there's no lower bound.
     * @property max The maximum allowed value (inclusive), or null if there's no upper bound.
     * @property precision The number of decimal places to consider.
     * @author Vad1mChK
     */
    class NumberInputValidator(
        private val min: Float? = null,
        private val max: Float? = null,
        private val precision: Int
    ) : AuthValidator<String> {
        override fun validate(value: String): AuthValidationResult {
            if (value.isEmpty()) {
                return AuthValidationResult.Failure(R.string.auth_valid_error_number_notempty)
            }

            return try {
                val decimalValue = BigDecimal(value).setScale(precision, RoundingMode.HALF_UP)
                val floatValue = decimalValue.toFloat()

                when {
                    min != null && floatValue < min ->
                        AuthValidationResult.Failure(
                            R.string.auth_valid_error_number_min,
                            min.format(precision)
                        )

                    max != null && floatValue > max ->
                        AuthValidationResult.Failure(
                            R.string.auth_valid_error_number_max,
                            max.format(precision)
                        )

                    else -> AuthValidationResult.Success
                }
            } catch (e: NumberFormatException) {
                AuthValidationResult.Failure(R.string.auth_valid_error_number_format)
            }
        }
    }
}