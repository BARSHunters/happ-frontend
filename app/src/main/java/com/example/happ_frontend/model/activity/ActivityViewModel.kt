package com.example.happ_frontend.model.activity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.happ_frontend.model.activity.communication.ActivityNetworkModule
import com.example.happ_frontend.model.activity.request.ActivityRequest
import com.example.happ_frontend.model.activity.response.Activity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ActivityViewModel : ViewModel() {
    private val activityApiService = ActivityNetworkModule.activityApiService
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    private val TAG = "ActivityViewModel"

    internal val _uiState = MutableStateFlow(ActivityUiState())
    val uiState: StateFlow<ActivityUiState> = _uiState.asStateFlow()

    init {
        loadActivitiesForDate(LocalDate.now())
    }

    fun loadActivitiesForDate(date: LocalDate) {
        viewModelScope.launch {
            Log.d(TAG, "Загрузка тренировок для даты: $date")
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val response = activityApiService.getActivities()
                if (response.isSuccessful) {
                    val activities = response.body()?.activities ?: emptyList()
                    Log.d(TAG, "Получено тренировок: ${activities.size}")
                    activities.forEach { activity ->
                        Log.d(TAG, "Тренировка: ${activity.name}, дата: ${activity.datetime}")
                    }
                    
                    val filteredActivities = activities.filter { activity ->
                        try {
                            val activityDateTime = LocalDateTime.parse(activity.datetime, dateTimeFormatter)
                            val activityDate = activityDateTime.toLocalDate()
                            Log.d(TAG, "Сравнение дат: $activityDate == $date")
                            activityDate == date
                        } catch (e: Exception) {
                            Log.e(TAG, "Ошибка парсинга даты: ${activity.datetime}", e)
                            false
                        }
                    }
                    Log.d(TAG, "Отфильтровано тренировок: ${filteredActivities.size}")
                    
                    val workouts = filteredActivities.map { activity ->
                        Workout(
                            time = activity.datetime,
                            name = activity.name,
                            calories = activity.calories
                        )
                    }
                    val activityDay = ActivityDay(date = date, workouts = workouts)
                    _uiState.value = _uiState.value.copy(
                        selectedDate = date,
                        currentActivityDay = activityDay,
                        isLoading = false,
                        error = null
                    )
                    Log.d(TAG, "UI обновлен. Тренировок в UI: ${activityDay.workouts.size}")
                } else {
                    Log.e(TAG, "Ошибка загрузки данных: ${response.code()}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Ошибка загрузки данных: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка сети: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Ошибка сети: ${e.message}"
                )
            }
        }
    }

    fun addNewWorkout(workout: Workout) {
        viewModelScope.launch {
            Log.d(TAG, "Добавление новой тренировки: ${workout.name}, время: ${workout.time}")
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val request = ActivityRequest(
                    name = workout.name,
                    datetime = workout.time,
                    calories = workout.calories
                )
                
                val response = activityApiService.createActivity(request)
                if (response.isSuccessful) {
                    Log.d(TAG, "Тренировка успешно создана")
                    val workoutDate = LocalDateTime.parse(workout.time, dateTimeFormatter).toLocalDate()
                    loadActivitiesForDate(workoutDate)
                } else {
                    Log.e(TAG, "Ошибка создания тренировки: ${response.code()}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Ошибка создания тренировки: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка сети: ${e.message}")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Ошибка сети: ${e.message}"
                )
            }
        }
    }

    fun getActivitySummary(workouts: List<Workout>): ActivitySummary {
        var totalCalories = 0
        workouts.forEach { workout ->
            totalCalories += workout.calories
        }
        return ActivitySummary(calories = totalCalories)
    }
}

data class ActivityUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val currentActivityDay: ActivityDay? = null,
    val activityHistory: List<ActivityDay> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class ActivityDay(
    val date: LocalDate,
    val workouts: List<Workout>
)

data class Workout(
    val time: String,
    val name: String,
    val calories: Int
)

data class ActivitySummary(
    val calories: Int
)

/**
 * Updates the data for a new workout being created
 */
fun ActivityViewModel.setNewWorkoutData(
    name: String,
    date: LocalDate,
    duration: Int,
    effort: String
) {
    // Выбираем случайный уровень нагрузки для разнообразия данных
    val effortLevels = listOf("Easy", "Moderate", "Hard", "Very Hard", "Maximum")
    val randomEffort = effortLevels.random()
    
    // Используем переданный параметр effort только для совместимости
    // Фактически мы будем использовать случайно сгенерированный уровень
    val actualEffort = randomEffort
    
    // Генерация данных по зонам активности на основе уровня нагрузки
    val activityZones = generateMockActivityZones(actualEffort)
    
    // Расчет интенсивности на основе распределения времени по зонам
    // Чем больше времени проведено в высоких зонах, тем выше интенсивность
    val zoneWeights = listOf(0.2f, 0.4f, 0.6f, 0.8f, 1.0f)  // Веса для каждой зоны
    val totalZoneTime = activityZones.sum().toFloat().coerceAtLeast(1f)
    
    val weightedIntensity = activityZones.mapIndexed { index, minutes ->
        val zoneWeight = if (index < zoneWeights.size) zoneWeights[index] else 0.5f
        minutes * zoneWeight
    }.sum() / totalZoneTime
    
    // Масштабируем интенсивность от 30 до 100
    val calculatedIntensity = (30 + (weightedIntensity * 70)).toInt().coerceIn(30, 100)
    
    // Генерация текущего времени (можно улучшить, если нужен более точный формат)
    val currentTime = LocalDateTime.now().withSecond(0).withNano(0)
    val formattedTime = DateTimeFormatter
        .ofPattern("hh:mm a")
        .format(currentTime)
        .lowercase()

    // Create a mock workout based on the provided data
    val newWorkout = Workout(
        name = name,
        time = formattedTime,
        calories = calculateEstimatedCalories(duration, actualEffort)
    )

    // Now add the workout to the UI state
    viewModelScope.launch {
        // Get current workouts or create empty list
        val currentWorkouts = _uiState.value.currentActivityDay?.workouts ?: emptyList()
        
        // Add new workout to the beginning of the list
        val updatedWorkouts = listOf(newWorkout) + currentWorkouts
        val activityDay = ActivityDay(date = date, workouts = updatedWorkouts)

        // Update UI state with the new workout added
        _uiState.value = _uiState.value.copy(
            selectedDate = date,
            currentActivityDay = activityDay,
            isLoading = false
        )
    }
}

// Helper functions for mock data generation

/**
 * Estimates calories burned based on duration and effort
 */
private fun calculateEstimatedCalories(duration: Int, effort: String): Int {
    val baseCaloriesPerMinute = 7
    
    val effortMultiplier = when (effort) {
        "Easy" -> 0.8
        "Moderate" -> 1.0
        "Hard" -> 1.2
        "Very Hard" -> 1.4
        "Maximum" -> 1.6
        else -> 1.0
    }

    return (baseCaloriesPerMinute * duration * effortMultiplier).toInt()
}

/**
 * Generates mock activity zones based on effort level
 */
private fun generateMockActivityZones(effort: String): List<Int> {
    return when (effort) {
        "Easy" -> listOf(15, 10, 5, 2, 0)
        "Moderate" -> listOf(10, 15, 12, 5, 1)
        "Hard" -> listOf(5, 12, 15, 10, 5)
        "Very Hard" -> listOf(3, 7, 12, 17, 10)
        "Maximum" -> listOf(2, 5, 10, 15, 20)
        else -> listOf(10, 10, 10, 10, 10)
    }
}

/**
 * Estimates average heart rate based on effort
 */
private fun calculateEstimatedHeartRate(effort: String): Int {
    val baseHeartRate = 125
    
    val effortAddition = when (effort) {
        "Easy" -> -20
        "Moderate" -> 0
        "Hard" -> 15
        "Very Hard" -> 25
        "Maximum" -> 35
        else -> 0
    }

    return baseHeartRate + effortAddition
}

/**
 * Estimates max heart rate based on effort
 */
private fun calculateEstimatedMaxHeartRate(effort: String): Int {
    val avgHeartRate = calculateEstimatedHeartRate(effort)

    val maxIncrease = when (effort) {
        "Easy" -> 15
        "Moderate" -> 20
        "Hard" -> 25
        "Very Hard" -> 30
        "Maximum" -> 40
        else -> 20
    }

    return avgHeartRate + maxIncrease
}

