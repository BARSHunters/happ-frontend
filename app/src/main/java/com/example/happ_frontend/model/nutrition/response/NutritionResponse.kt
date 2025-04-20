package com.example.happ_frontend.model.nutrition.response

import java.time.LocalDate
import java.time.LocalTime

data class NutritionResponse(
    val meals: List<Meal>
)

data class Meal(
    val id: String,
    val name: String,
    val type: MealType,
    val calories: Int,
    val proteins: Int,
    val fats: Int,
    val carbs: Int,
    val date: String, // формат: "yyyy-MM-dd"
    val time: String,
    val portionSize: Int, // в граммах
    val recipeUrl: String
)

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK
} 