package com.example.happ_frontend.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

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
                        name = "Avocado Toast with Eggs",
                        calories = 450,
                        protein = 22,
                        fat = 25,
                        carbs = 35,
                        portionSize = "250g",
                        imageUrl = "https://example.com/avocado_toast.jpg", // Mock image URL
                        recipeUrl = "https://example.com/recipes/avocado_toast",
                        mealType = MealType.BREAKFAST
                    ),
                    Meal(
                        time = "12:30 pm",
                        name = "Grilled Chicken Shawarma with Vegetables",
                        calories = 536,
                        protein = 38,
                        fat = 22,
                        carbs = 40,
                        portionSize = "350g",
                        imageUrl = "https://example.com/shawarma.jpg",
                        recipeUrl = "https://example.com/recipes/chicken_shawarma",
                        mealType = MealType.LUNCH
                    ),
                    Meal(
                        time = "07:00 pm",
                        name = "Salmon with Roasted Vegetables",
                        calories = 520,
                        protein = 34,
                        fat = 28,
                        carbs = 26,
                        portionSize = "300g",
                        imageUrl = "https://example.com/salmon.jpg",
                        recipeUrl = "https://example.com/recipes/salmon_roasted_vegetables",
                        mealType = MealType.DINNER
                    ),
                    Meal(
                        time = "04:00 pm",
                        name = "Greek Yogurt with Berries",
                        calories = 200,
                        protein = 15,
                        fat = 8,
                        carbs = 20,
                        portionSize = "150g",
                        mealType = MealType.SNACK
                    )
                )
            } else if (date.isBefore(LocalDate.now())) {
                // Past menu
                listOf(
                    Meal(
                        time = "07:30 am",
                        name = "Oatmeal with Fruit and Nuts",
                        calories = 320,
                        protein = 12,
                        fat = 10,
                        carbs = 48,
                        portionSize = "280g",
                        imageUrl = "https://example.com/oatmeal.jpg",
                        recipeUrl = "https://example.com/recipes/oatmeal_fruit_nuts",
                        mealType = MealType.BREAKFAST
                    ),
                    Meal(
                        time = "01:00 pm",
                        name = "Quinoa Bowl with Grilled Vegetables",
                        calories = 450,
                        protein = 20,
                        fat = 15,
                        carbs = 60,
                        portionSize = "320g",
                        imageUrl = "https://example.com/quinoa_bowl.jpg",
                        recipeUrl = "https://example.com/recipes/quinoa_bowl",
                        mealType = MealType.LUNCH
                    ),
                    Meal(
                        time = "06:30 pm",
                        name = "Baked Cod with Sweet Potato",
                        calories = 380,
                        protein = 30,
                        fat = 12,
                        carbs = 30,
                        portionSize = "300g",
                        imageUrl = "https://example.com/baked_cod.jpg",
                        recipeUrl = "https://example.com/recipes/baked_cod",
                        mealType = MealType.DINNER
                    ),
                    Meal(
                        time = "03:30 pm",
                        name = "Mixed Fruit Salad",
                        calories = 120,
                        protein = 2,
                        fat = 0,
                        carbs = 30,
                        portionSize = "150g",
                        mealType = MealType.SNACK
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

    fun createNewMenuForToday() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Switch to today's date
            val today = LocalDate.now()

            // TODO: Call API to create a new menu
            // Example API call - implement actual API client later
            // val response = api.createNutritionMenu(today.format(DateTimeFormatter.ISO_DATE))

            // Mock data for a newly created menu
            val meals = listOf(
                Meal(
                    time = "08:00 am",
                    name = "Spinach and Mushroom Omelet",
                    calories = 380,
                    protein = 25,
                    fat = 22,
                    carbs = 15,
                    portionSize = "220g",
                    imageUrl = "https://example.com/omelet.jpg",
                    recipeUrl = "https://example.com/recipes/spinach_mushroom_omelet",
                    mealType = MealType.BREAKFAST
                ),
                Meal(
                    time = "12:30 pm",
                    name = "Sushi Bento Box",
                    calories = 520,
                    protein = 28,
                    fat = 14,
                    carbs = 65,
                    portionSize = "350g",
                    imageUrl = "https://example.com/sushi.jpg",
                    recipeUrl = "https://example.com/recipes/sushi_bento",
                    mealType = MealType.LUNCH
                ),
                Meal(
                    time = "07:00 pm",
                    name = "Vegetable Stir Fry with Tofu",
                    calories = 420,
                    protein = 22,
                    fat = 18,
                    carbs = 40,
                    portionSize = "320g",
                    imageUrl = "https://example.com/stir_fry.jpg",
                    recipeUrl = "https://example.com/recipes/tofu_stir_fry",
                    mealType = MealType.DINNER
                ),
                Meal(
                    time = "04:00 pm",
                    name = "Hummus with Carrot Sticks",
                    calories = 180,
                    protein = 8,
                    fat = 10,
                    carbs = 16,
                    portionSize = "120g",
                    mealType = MealType.SNACK
                )
            )

            val mealDay = MealDay(date = today, meals = meals)

            // After API call, update UI state to today with the new menu
            _uiState.value = _uiState.value.copy(
                selectedDate = today,
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

enum class MealType {
    BREAKFAST,
    LUNCH,
    DINNER,
    SNACK
}

data class Meal(
    val time: String,
    val name: String,
    val calories: Int,
    val protein: Int? = null,
    val fat: Int? = null,
    val carbs: Int? = null,
    val portionSize: String? = "300g",
    val imageUrl: String? = null,
    val recipeUrl: String? = null,
    val mealType: MealType = MealType.SNACK
)

data class NutritionSummary(
    val calories: Int,
    val protein: Int,
    val fat: Int,
    val carbs: Int
)