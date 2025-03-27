package com.example.happ_frontend

import android.app.Application

class HappFrontendApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        println("Application started")
        container = AppDataContainer(this)
    }
}