package com.example.happ_frontend.model.activity.request

data class ActivityRequest(
    val name: String,
    val datetime: String, // формат: "yyyy-MM-dd HH:mm"
    val calories: Int
)

data class ExerciseRequest(
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: Double? // опционально, если упражнение с весом
) 