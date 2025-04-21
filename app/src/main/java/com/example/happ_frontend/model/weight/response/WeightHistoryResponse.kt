package com.example.happ_frontend.model.weight.response

import java.time.LocalDateTime

data class WeightHistoryResponse(
    val username: String, val weightHistory: Map<LocalDateTime, Double>
)
