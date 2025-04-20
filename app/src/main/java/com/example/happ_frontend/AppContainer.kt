package com.example.happ_frontend

import android.content.Context
import com.example.happ_frontend.model.notifications.data.MyNotificationsRepository
import com.example.happ_frontend.model.notifications.data.NotificationsRepository
import com.example.happ_frontend.model.weight.data.MyWeightHistoryRepository
import com.example.happ_frontend.model.weight.data.WeightHistoryRepository

interface AppContainer {
    val notificationsRepository: NotificationsRepository
    val weightHistoryRepository: WeightHistoryRepository
}

class AppDataContainer(private val context: Context) : AppContainer{
    override val notificationsRepository: NotificationsRepository by lazy {
        MyNotificationsRepository(HappFrontendDatabase.getDatabase(context).notificationDAO())
    }
    override val weightHistoryRepository: WeightHistoryRepository by lazy {
        MyWeightHistoryRepository(HappFrontendDatabase.getDatabase(context).weightHistoryDAO())
    }
}