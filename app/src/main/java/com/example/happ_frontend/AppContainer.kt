package com.example.happ_frontend

import android.content.Context
import com.example.happ_frontend.model.notifications.data.MyNotificationsRepository
import com.example.happ_frontend.model.notifications.data.NotificationsDatabase
import com.example.happ_frontend.model.notifications.data.NotificationsRepository

interface AppContainer {
    val notificationsRepository: NotificationsRepository
}

class AppDataContainer(private val context: Context) : AppContainer{
    override val notificationsRepository: NotificationsRepository by lazy {
        MyNotificationsRepository(NotificationsDatabase.getDatabase(context).notificationDAO())
    }
}