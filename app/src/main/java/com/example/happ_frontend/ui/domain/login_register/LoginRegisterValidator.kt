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
sealed interface LoginRegisterValidator<T> {
    /**
     * Validates the given value according to the specific validator's rules.
     *
     * @param value The value to be validated.
     * @return A [LoginRegisterValidationResult] indicating the success or failure of the validation.
     * @author Vad1mChK
     */
    fun validate(value: T): LoginRegisterValidationResult

    /**
     * Validates if an integer is within a specified range.
     *
     * @property min The minimum allowed value (inclusive), or null if there's no lower bound.
     * @property max The maximum allowed value (inclusive), or null if there's no upper bound.
     * @author Vad1mChK
     */
    class IntInRangeValidator(
        val min: Int? = null, val max: Int? = null
    ) : LoginRegisterValidator<Int> {
        override fun validate(value: Int): LoginRegisterValidationResult {
            if (min != null && value < min) {
                return LoginRegisterValidationResult.Failure(R.string.auth_valid_error_number_min)
            }
            if (max != null && value > max) {
                return LoginRegisterValidationResult.Failure(R.string.auth_valid_error_number_max)
            }
            return LoginRegisterValidationResult.Success
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
    ) : LoginRegisterValidator<Float> {
        override fun validate(value: Float): LoginRegisterValidationResult {
            if (min != null && value < min) {
                return LoginRegisterValidationResult.Failure(R.string.auth_valid_error_number_min)
            }
            if (max != null && value > max) {
                return LoginRegisterValidationResult.Failure(R.string.auth_valid_error_number_max)
            }
            return LoginRegisterValidationResult.Success
        }
    }

    /**
     * Validates a username string based on length and regex pattern.
     * @author Vad1mChK
     */
    class UsernameValidator : LoginRegisterValidator<String> {
        companion object {
            private const val USERNAME_REGEX_STRING = "^[a-zA-Z_][a-zA-Z0-9_]*\$"
            private const val USERNAME_MIN_LENGTH = 5
        }

        private val regex = Regex(USERNAME_REGEX_STRING)

        override fun validate(value: String): LoginRegisterValidationResult {
            if (value.length < USERNAME_MIN_LENGTH) {
                return LoginRegisterValidationResult.Failure(
                    R.string.auth_valid_error_username_length,
                    USERNAME_MIN_LENGTH
                )
            }
            if (!regex.matches(value)) {
                return LoginRegisterValidationResult.Failure(
                    R.string.auth_valid_error_username_regex
                )
            }
            return LoginRegisterValidationResult.Success
        }
    }

    /**
     * Validates a password string based on minimum length.
     * @author Vad1mChK
     */
    class PasswordValidator : LoginRegisterValidator<String> {
        companion object {
            private const val PASSWORD_MIN_LENGTH = 8
        }

        override fun validate(value: String): LoginRegisterValidationResult {
            if (value.length < PASSWORD_MIN_LENGTH) {
                return LoginRegisterValidationResult.Failure(
                    R.string.auth_valid_error_password_length,
                    PASSWORD_MIN_LENGTH
                )
            }
            return LoginRegisterValidationResult.Success
        }
    }

    /**
     * Validates if a password matches a previously entered password.
     *
     * @property firstPassword The password to match against.
     * @author Vad1mChK
     */
    class PasswordMatchValidator(val firstPassword: String) : LoginRegisterValidator<String> {
        override fun validate(value: String): LoginRegisterValidationResult {
            if (value != firstPassword) {
                return LoginRegisterValidationResult.Failure(
                    R.string.auth_valid_error_password_match
                )
            }
            return LoginRegisterValidationResult.Success
        }
    }

    /**
     * Validates a name string based on non-emptiness and regex pattern.
     * @author Vad1mChK
     */
    class NameValidator : LoginRegisterValidator<String> {
        companion object {
            private const val NAME_REGEX_STRING =
                "^\\p{L}(?:[-']?\\p{L}+)*(?: +\\p{L}(?:[-']?\\p{L}+)*)*\$"
        }

        private val regex = Regex(NAME_REGEX_STRING)

        override fun validate(value: String): LoginRegisterValidationResult {
            if (value.isEmpty()) {
                return LoginRegisterValidationResult.Failure(
                    R.string.auth_valid_error_name_notempty
                )
            }
            if (regex.matchEntire(value) == null) {
                return LoginRegisterValidationResult.Failure(
                    R.string.auth_valid_error_name_regex
                )
            }
            return LoginRegisterValidationResult.Success
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
    ) : LoginRegisterValidator<String> {
        override fun validate(value: String): LoginRegisterValidationResult {
            if (value.isEmpty()) {
                return LoginRegisterValidationResult.Failure(R.string.auth_valid_error_number_notempty)
            }

            return try {
                val decimalValue = BigDecimal(value).setScale(precision, RoundingMode.HALF_UP)
                val floatValue = decimalValue.toFloat()

                when {
                    min != null && floatValue < min ->
                        LoginRegisterValidationResult.Failure(
                            R.string.auth_valid_error_number_min,
                            min.format(precision)
                        )

                    max != null && floatValue > max ->
                        LoginRegisterValidationResult.Failure(
                            R.string.auth_valid_error_number_max,
                            max.format(precision)
                        )

                    else -> LoginRegisterValidationResult.Success
                }
            } catch (e: NumberFormatException) {
                LoginRegisterValidationResult.Failure(R.string.auth_valid_error_number_format)
            }
        }
    }
}