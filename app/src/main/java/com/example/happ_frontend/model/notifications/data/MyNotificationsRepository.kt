package com.example.happ_frontend.model.notifications.data


class MyNotificationsRepository(
    private val notificationDAO: NotificationDAO
) : NotificationsRepository {
    override suspend fun getAllNotificationsStream(): List<NotificationEntity> = notificationDAO.getAllNotifications()

    override suspend fun insertNotification(notificationEntity: NotificationEntity) = notificationDAO.insert(notificationEntity)

    override suspend fun deleteAll() = notificationDAO.deleteAllNotifications()
}