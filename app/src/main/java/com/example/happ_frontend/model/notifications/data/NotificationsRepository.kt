package com.example.happ_frontend.model.notifications.data

interface NotificationsRepository {
    suspend fun getAllNotificationsStream(): List<NotificationEntity>
    suspend fun insertNotification(notificationEntity: NotificationEntity)
    suspend fun deleteAll()
}