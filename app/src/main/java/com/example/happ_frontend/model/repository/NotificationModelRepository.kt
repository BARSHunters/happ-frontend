package com.example.happ_frontend.model.repository

import com.example.happ_frontend.model.notifications.NotificationData
import java.time.LocalDate

class NotificationModelRepository {
    fun getAll() : MutableList<NotificationData>{
        return mutableListOf(
            NotificationData(
                "Activity",
                "",
                LocalDate.now()
            ),
            NotificationData(
                "FriendRequest",
                "Zerumi",
                LocalDate.now()
            ),
            NotificationData(
                "FriendRequest",
                "Hamza",
                LocalDate.now()
            ),
            NotificationData(
                "Achievement",
                "Bigger",
                LocalDate.now()
            ),
            NotificationData(
                "Lolasdasd",
                "AHAHAHAHA",
                LocalDate.now()
            )
        )
    }
}