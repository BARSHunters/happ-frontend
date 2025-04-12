package com.example.happ_frontend.model.login_register.data

import android.content.Context
import androidx.core.content.edit

class AuthSharedPreferencesEditor(context: Context) {
    companion object {
        private const val FILENAME = "UserAuthPreferences"
        private const val KEY_USERNAME = "username"
        private const val KEY_JWT = "jwt"
    }

    private val appContext = context.applicationContext

    private val authPreferences by lazy {
        appContext.getSharedPreferences(FILENAME, Context.MODE_PRIVATE)
    }

    var username: String?
        get() = authPreferences.getString(KEY_USERNAME, null)
        set(value) = authPreferences.edit { putString(KEY_USERNAME, value) }

    var jwt: String?
        get() = authPreferences.getString(KEY_JWT, null)
        set(value) = authPreferences.edit { putString(KEY_JWT, value) }

    val loggedIn get() = !jwt.isNullOrBlank()

    fun clearUserData() {
        authPreferences.edit { clear() }
    }
}