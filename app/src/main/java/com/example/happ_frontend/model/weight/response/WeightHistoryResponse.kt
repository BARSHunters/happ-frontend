package com.example.happ_frontend.model.weight.response

import com.example.happ_frontend.model.weight.communication.WeightHistoryApiService
import java.time.LocalDateTime

data class WeightHistoryResponse(
    val userId: String, val weightHistory: Map<LocalDateTime, Double>
)
