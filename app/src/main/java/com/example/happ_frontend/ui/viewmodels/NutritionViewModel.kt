package com.example.happ_frontend.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class NutritionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NutritionUiState())
    val uiState: StateFlow<NutritionUiState> = _uiState.asStateFlow()

    init {
        loadNutritionMenuForDate(LocalDate.now())
    }

    fun loadNutritionMenuForDate(date: LocalDate) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // TODO: Call API to get nutrition menu for the selected date
            // Example API call - implement actual API client later
            // val response = api.getNutritionMenu(date.format(DateTimeFormatter.ISO_DATE))

            // Mock data based on the date
            val meals = if (date == LocalDate.now()) {
                // Today's menu
                listOf(
                    Meal(
                        time = "08:00 am",
                        name = "Cheeseburger",
                        calories = 450,
                        protein = 22,
                        fat = 25,
                        carbs = 35
                    ),
                    Meal(
                        time = "12:00 pm",
                        name = "Shawarma",
                        calories = 536,
                        protein = 28,
                        fat = 22,
                        carbs = 50
                    ),
                    Meal(
                        time = "07:00 pm",
                        name = "Pizza",
                        calories = 680,
                        protein = 24,
                        fat = 30,
                        carbs = 76
                    ),
                    Meal(
                        time = "04:00 pm",
                        name = "Snack",
                        calories = 200,
                        protein = 5,
                        fat = 8,
                        carbs = 25
                    )
                )
            } else if (date.isBefore(LocalDate.now())) {
                // Past menu
                listOf(
                    Meal(
                        time = "08:00 am",
                        name = "Oatmeal",
                        calories = 320,
                        protein = 12,
                        fat = 6,
                        carbs = 58
                    ),
                    Meal(
                        time = "12:00 pm",
                        name = "Sandwich",
                        calories = 450,
                        protein = 20,
                        fat = 15,
                        carbs = 45
                    ),
                    Meal(
                        time = "07:00 pm",
                        name = "Salad",
                        calories = 380,
                        protein = 15,
                        fat = 12,
                        carbs = 30
                    ),
                    Meal(
                        time = "04:00 pm",
                        name = "Fruit",
                        calories = 120,
                        protein = 2,
                        fat = 0,
                        carbs = 30
                    )
                )
            } else {
                // Future - empty list since menu hasn't been created yet
                emptyList()
            }

            val mealDay = MealDay(date = date, meals = meals)

            // Update UI state with the fetched or mock data
            _uiState.value = _uiState.value.copy(
                selectedDate = date,
                currentMealDay = mealDay,
                isLoading = false
            )
        }
    }

    fun createNewMenu() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // TODO: Call API to create a new menu
            // Example API call - implement actual API client later
            // val response = api.getNutritionMenu()

            // Mock data for a newly created menu
            val meals = listOf(
                Meal(
                    time = "08:00 am",
                    name = "Pasta",
                    calories = 420,
                    protein = 18,
                    fat = 10,
                    carbs = 65
                ),
                Meal(
                    time = "12:00 pm",
                    name = "Sushi",
                    calories = 380,
                    protein = 20,
                    fat = 8,
                    carbs = 45
                ),
                Meal(
                    time = "07:00 pm",
                    name = "Rice with Vegetables",
                    calories = 420,
                    protein = 12,
                    fat = 6,
                    carbs = 80
                ),
                Meal(
                    time = "04:00 pm",
                    name = "Greek Yogurt",
                    calories = 150,
                    protein = 15,
                    fat = 5,
                    carbs = 8
                )
            )

            val mealDay = MealDay(date = LocalDate.now(), meals = meals)

            // After API call, update UI state
            _uiState.value = _uiState.value.copy(
                currentMealDay = mealDay,
                isLoading = false
            )
        }
    }

    fun getTotalNutrition(meals: List<Meal>): NutritionSummary {
        var totalCalories = 0
        var totalProtein = 0
        var totalFat = 0
        var totalCarbs = 0

        meals.forEach { meal ->
            totalCalories += meal.calories
            totalProtein += meal.protein ?: 0
            totalFat += meal.fat ?: 0
            totalCarbs += meal.carbs ?: 0
        }

        return NutritionSummary(
            calories = totalCalories,
            protein = totalProtein,
            fat = totalFat,
            carbs = totalCarbs
        )
    }
}

data class NutritionUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val currentMealDay: MealDay? = null,
    val menuHistory: List<MealDay> = emptyList(),
    val isLoading: Boolean = false
)

data class MealDay(
    val date: LocalDate,
    val meals: List<Meal>
)

data class Meal(
    val time: String,
    val name: String,
    val calories: Int,
    val protein: Int? = null,
    val fat: Int? = null,
    val carbs: Int? = null,
    val portionSize: String? = "300g",
    val imageUrl: String? = null,
    val recipeUrl: String? = null
)

data class NutritionSummary(
    val calories: Int,
    val protein: Int,
    val fat: Int,
    val carbs: Int
)