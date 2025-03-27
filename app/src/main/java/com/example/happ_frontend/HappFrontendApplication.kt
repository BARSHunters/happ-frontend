package com.example.happ_frontend

import android.app.Application

class HappFrontendApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        println("Application started")
    }
}