package com.example.happ_frontend.model.weight.request

data class APIGatewayToWeightHistoryRequest(
    val userId: String, // TODO figure out what userId is
    val weightControlWish: String = "keep",
)
