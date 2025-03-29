package com.example.happ_frontend.model.notifications.data

import com.example.happ_frontend.model.notifications.NotificationData
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC

object NotificationConverter {
    fun fromEntity(notificationEntity: NotificationEntity) : NotificationData{
        return NotificationData(
            notificationEntity.type,
            notificationEntity.data,
            LocalDateTime.ofEpochSecond(notificationEntity.date, 0, UTC)
        )
    }

    fun toEntity(notificationData: NotificationData) : NotificationEntity {
        return NotificationEntity(
            type = notificationData.type,
            data = notificationData.data,
            date = notificationData.date.toEpochSecond(UTC)
        )
    }
}