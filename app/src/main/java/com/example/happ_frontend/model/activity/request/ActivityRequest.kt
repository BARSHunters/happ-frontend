package com.example.happ_frontend.model.activity.request

data class ActivityRequest(
    val name: String,
    val datetime: String, // формат: "yyyy-MM-dd HH:mm"
    val calories: Int,
    val intensityZones: List<Int> // время в минутах для каждой зоны: [очень легкая, легкая, умеренная, высокая, максимальная]
)

data class ExerciseRequest(
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: Double? // опционально, если упражнение с весом
) 