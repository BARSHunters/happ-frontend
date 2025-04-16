package com.example.happ_frontend.model.activity.response

data class ActivityResponse(
    val activities: List<Activity>
)

data class Activity(
    val id: String,
    val name: String,
    val datetime: String, // формат: "yyyy-MM-dd HH:mm"
    val calories: Int
)

data class Exercise(
    val id: String,
    val name: String,
    val sets: Int,
    val reps: Int,
    val weight: Double?
) 