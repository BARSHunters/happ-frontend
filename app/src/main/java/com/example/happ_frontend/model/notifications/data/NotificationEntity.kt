package com.example.happ_frontend.model.notifications.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val type: String,
    val data: String,
    val date: Long
)