package com.example.happ_frontend.model.login_register.request

/**
 * Data transfer object for user login credentials.
 *
 * This class encapsulates the necessary information required for authenticating a user
 * during the login process.
 *
 * @property username The unique user-selected name for the account.
 * @property password The secret authentication string associated with the account.
 */
data class LoginDto(
    val username: String,
    val password: String
)
