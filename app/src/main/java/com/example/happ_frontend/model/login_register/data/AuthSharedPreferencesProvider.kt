package com.example.happ_frontend.model.login_register.data

import android.content.Context

object AuthSharedPreferencesProvider {
    var editor: AuthSharedPreferencesEditor? = null
        private set

    fun initialize(context: Context) {
        editor = AuthSharedPreferencesEditor(context)
    }
}
