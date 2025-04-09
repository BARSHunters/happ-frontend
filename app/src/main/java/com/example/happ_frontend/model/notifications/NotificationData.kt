package com.example.happ_frontend.model.notifications

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

data class NotificationData(
    var type: String,
    //TODO Сделать data не String a Map<String, String>
    var data: String,
    var date: LocalDateTime
)

object localDateTimeFormat{
    val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
}