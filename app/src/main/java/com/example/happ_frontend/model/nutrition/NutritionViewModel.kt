package com.example.happ_frontend.model.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.happ_frontend.model.login_register.data.AuthSharedPreferencesEditor
import com.example.happ_frontend.model.login_register.data.AuthSharedPreferencesProvider
import com.example.happ_frontend.model.nutrition.communication.NutritionApiService
import com.example.happ_frontend.model.nutrition.communication.NutritionNetworkModule
import com.example.happ_frontend.model.nutrition.request.NutritionRequest
import com.example.happ_frontend.model.nutrition.response.Meal as ApiMeal
import com.example.happ_frontend.model.nutrition.response.MealType as ApiMealType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.time.temporal.TemporalAdjusters
import java.time.DayOfWeek
import java.util.*

class NutritionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NutritionUiState())
    val uiState: StateFlow<NutritionUiState> = _uiState.asStateFlow()

    private val apiService = NutritionNetworkModule.apiService
    private val authPrefs: AuthSharedPreferencesEditor = AuthSharedPreferencesProvider.editor
        ?: throw IllegalStateException("AuthSharedPreferencesEditor not initialized yet")

    // Используем тот же подход, что и в календаре
    private var currentWeekStart: LocalDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    private var currentWeekEnd: LocalDate = currentWeekStart.plusDays(6)

    init {
        loadNutritionForWeek(currentWeekStart, currentWeekEnd)
    }

    fun loadInitialData() {
        val today = LocalDate.now()
        loadNutritionForDate(today)
    }

    fun loadNutritionForWeek(startDate: LocalDate, endDate: LocalDate) {
        // Если запрашиваемая неделя уже загружена, не делаем новый запрос
        if (startDate == currentWeekStart && endDate == currentWeekEnd && _uiState.value.weekMeals.isNotEmpty()) {
            return
        }

        currentWeekStart = startDate
        currentWeekEnd = endDate

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val token = authPrefs.jwt ?: throw IllegalStateException("Токен авторизации не найден")
                
                val response = apiService.getNutritionsByWeek(
                    token = "Bearer $token",
                    startDate = startDate.format(DateTimeFormatter.ISO_DATE),
                    endDate = endDate.format(DateTimeFormatter.ISO_DATE)
                )
                
                if (!response.isSuccessful) {
                    throw Exception("Ошибка при загрузке данных: ${response.code()}")
                }
                
                val apiMeals = response.body()?.meals ?: emptyList()
                val weekMeals = mutableMapOf<LocalDate, List<Meal>>()
                
                // Инициализируем пустые списки для всех дней недели
                var currentDate = startDate
                while (currentDate <= endDate) {
                    weekMeals[currentDate] = emptyList()
                    currentDate = currentDate.plusDays(1)
                }
                
                // Заполняем данные для каждого дня
                apiMeals.forEach { apiMeal ->
                    val meal = Meal(
                        time = apiMeal.time,
                        name = apiMeal.name,
                        calories = apiMeal.calories,
                        protein = apiMeal.proteins,
                        fat = apiMeal.fats,
                        carbs = apiMeal.carbs,
                        portionSize = "${apiMeal.portionSize}г",
                        recipeUrl = apiMeal.recipeUrl,
                        mealType = when (apiMeal.type) {
                            ApiMealType.BREAKFAST -> MealType.BREAKFAST
                            ApiMealType.LUNCH -> MealType.LUNCH
                            ApiMealType.DINNER -> MealType.DINNER
                            ApiMealType.SNACK -> MealType.SNACK
                        }
                    )
                    val date = LocalDate.parse(apiMeal.date, DateTimeFormatter.ISO_DATE)
                    val meals = weekMeals[date] ?: emptyList()
                    weekMeals[date] = meals + meal
                }
                
                _uiState.update { 
                    it.copy(
                        weekMeals = weekMeals,
                        currentWeekStart = startDate,
                        currentWeekEnd = endDate,
                        isLoading = false,
                        error = null
                    )
                }
                
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = e.message ?: "Неизвестная ошибка"
                ) }
            }
        }
    }

    fun loadNutritionForDate(date: LocalDate) {
        // Проверяем, находится ли дата в текущей загруженной неделе
        if (date in currentWeekStart..currentWeekEnd) {
            val meals = _uiState.value.weekMeals[date] ?: emptyList()
            val mealDay = MealDay(
                date = date,
                meals = meals
            )
            _uiState.update { 
                it.copy(
                    selectedDate = date,
                    currentMealDay = mealDay,
                    error = null
                )
            }
        } else {
            // Если дата вне текущей недели, загружаем новую неделю
            val weekStart = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val weekEnd = weekStart.plusDays(6)
            
            viewModelScope.launch {
                _uiState.update { it.copy(isLoading = true, error = null) }
                
                try {
                    val token = authPrefs.jwt ?: throw IllegalStateException("Токен авторизации не найден")
                    
                    val response = apiService.getNutritionsByWeek(
                        token = "Bearer $token",
                        startDate = weekStart.format(DateTimeFormatter.ISO_DATE),
                        endDate = weekEnd.format(DateTimeFormatter.ISO_DATE)
                    )
                    
                    if (response.isSuccessful) {
                        val apiMeals = response.body()?.meals ?: emptyList()
                        val weekMeals = mutableMapOf<LocalDate, List<Meal>>()
                        
                        // Инициализируем пустые списки для всех дней недели
                        var currentDate = weekStart
                        while (currentDate <= weekEnd) {
                            weekMeals[currentDate] = emptyList()
                            currentDate = currentDate.plusDays(1)
                        }
                        
                        // Заполняем данные для каждого дня
                        apiMeals.forEach { apiMeal ->
                            val meal = Meal(
                                time = apiMeal.time,
                                name = apiMeal.name,
                                calories = apiMeal.calories,
                                protein = apiMeal.proteins,
                                fat = apiMeal.fats,
                                carbs = apiMeal.carbs,
                                portionSize = "${apiMeal.portionSize}г",
                                recipeUrl = apiMeal.recipeUrl,
                                mealType = when (apiMeal.type) {
                                    ApiMealType.BREAKFAST -> MealType.BREAKFAST
                                    ApiMealType.LUNCH -> MealType.LUNCH
                                    ApiMealType.DINNER -> MealType.DINNER
                                    ApiMealType.SNACK -> MealType.SNACK
                                }
                            )
                            val mealDate = LocalDate.parse(apiMeal.date, DateTimeFormatter.ISO_DATE)
                            val meals = weekMeals[mealDate] ?: emptyList()
                            weekMeals[mealDate] = meals + meal
                        }
                        
                        // Обновляем состояние с новыми данными
                        _uiState.update { 
                            it.copy(
                                weekMeals = weekMeals,
                                currentWeekStart = weekStart,
                                currentWeekEnd = weekEnd,
                                selectedDate = date,
                                currentMealDay = MealDay(
                                    date = date,
                                    meals = weekMeals[date] ?: emptyList()
                                ),
                                isLoading = false,
                                error = null
                            )
                        }
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = "Ошибка загрузки данных: ${response.code()}"
                        ) }
                    }
                } catch (e: Exception) {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = "Ошибка сети: ${e.message}"
                    ) }
                }
            }
        }
    }

    fun createNewMenuForToday() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val selectedDate = _uiState.value.selectedDate
                val token = authPrefs.jwt ?: throw IllegalStateException("Токен авторизации не найден")
                
                val response = apiService.generateNutrition(
                    token = "Bearer $token",
                    request = NutritionRequest(date = selectedDate)
                )

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val apiMeals = responseBody?.meals ?: emptyList()
                        val meals = apiMeals.map { apiMeal ->
                            Meal(
                                time = apiMeal.time,
                                name = apiMeal.name,
                                calories = apiMeal.calories,
                                protein = apiMeal.proteins,
                                fat = apiMeal.fats,
                                carbs = apiMeal.carbs,
                                portionSize = "${apiMeal.portionSize}г",
                                recipeUrl = apiMeal.recipeUrl,
                                mealType = when (apiMeal.type) {
                                    ApiMealType.BREAKFAST -> MealType.BREAKFAST
                                    ApiMealType.LUNCH -> MealType.LUNCH
                                    ApiMealType.DINNER -> MealType.DINNER
                                    ApiMealType.SNACK -> MealType.SNACK
                                    else -> MealType.SNACK
                                }
                            )
                        }
                        
                        // Обновляем данные для выбранной даты
                        val currentWeekMeals = _uiState.value.weekMeals.toMutableMap()
                        currentWeekMeals[selectedDate] = meals
                        
                        _uiState.update { 
                            it.copy(
                                weekMeals = currentWeekMeals,
                                currentMealDay = MealDay(
                                    date = selectedDate,
                                    meals = meals
                                ),
                                isLoading = false
                            )
                        }
                    } else {
                        _uiState.update { it.copy(
                            isLoading = false,
                            error = "Ошибка: пустой ответ от сервера"
                        ) }
                    }
                } else {
                    _uiState.update { it.copy(
                        isLoading = false,
                        error = "Ошибка генерации меню: ${response.code()}"
                    ) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(
                    isLoading = false,
                    error = "Ошибка сети: ${e.message}"
                ) }
            }
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
    val isLoading: Boolean = false,
    val weekMeals: Map<LocalDate, List<Meal>> = emptyMap(),
    val currentWeekStart: LocalDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
    val currentWeekEnd: LocalDate = currentWeekStart.plusDays(6),
    val error: String? = null
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
    val portionSize: String,
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