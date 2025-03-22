package com.example.happ_frontend.ui.domain.login_register

import com.example.happ_frontend.R
import java.math.BigDecimal
import java.math.RoundingMode

sealed interface LoginRegisterValidator<T> {
    fun validate(value: T): LoginRegisterValidationResult

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

    // Add to LoginRegisterValidator class
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