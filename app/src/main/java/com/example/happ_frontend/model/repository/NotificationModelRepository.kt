package com.example.happ_frontend.model.repository

import com.example.happ_frontend.model.notifications.FireBaseNotificationService
import com.example.happ_frontend.model.notifications.NotificationData
import java.time.LocalDateTime

class NotificationModelRepository {
    fun getAll() : MutableList<NotificationData>{
//        return FireBaseNotificationService.localNotification.notifications
        return mutableListOf(
            NotificationData(
                "Activity",
                "",
                LocalDateTime.now()
            ),
            NotificationData(
                "FriendRequest",
                "Zerumi",
                LocalDateTime.now()
            ),
            NotificationData(
                "FriendRequest",
                "Hamza",
                LocalDateTime.now()
            ),
            NotificationData(
                "Achievement",
                "Bigger",
                LocalDateTime.now()
            ),
            NotificationData(
                "Lolasdasd",
                "AHAHAHAHA",
                LocalDateTime.now()
            )
        )
    }
}