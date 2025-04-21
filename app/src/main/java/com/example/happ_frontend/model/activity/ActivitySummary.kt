package com.example.happ_frontend.model.activity

data class ActivitySummary(
    val calories: Int,
    val met: Double,
    val avgHeartRate: Double,
    val maxHeartRate: Int,
    val totalDuration: Int // в минутах
) 