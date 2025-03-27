package com.example.happ_frontend.model.notifications

import java.time.LocalDate

data class NotificationData(
    var type: String,
    //TODO Сделать data не String a Map<String, String>
    var data: String,
    var date: LocalDate
)
