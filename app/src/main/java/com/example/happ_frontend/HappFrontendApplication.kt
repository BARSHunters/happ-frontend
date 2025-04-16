package com.example.happ_frontend

import android.app.Application
import com.example.happ_frontend.model.login_register.data.AuthSharedPreferencesProvider

class HappFrontendApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        println("Application started")

        AuthSharedPreferencesProvider.initialize(this)
        container = AppDataContainer(this)
    }
}