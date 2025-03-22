package com.example.happ_frontend.model.notifications

import java.time.LocalDate

data class NotificationData(
    var type: String,
    var data: String,
    var date: LocalDate
)
