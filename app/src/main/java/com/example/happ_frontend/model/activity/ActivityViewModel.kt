package com.example.happ_frontend.model.activity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.happ_frontend.model.activity.communication.ActivityNetworkModule
import com.example.happ_frontend.model.activity.request.ActivityRequest
import com.example.happ_frontend.model.login_register.data.AuthSharedPreferencesEditor
import com.example.happ_frontend.model.login_register.data.AuthSharedPreferencesProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

class ActivityViewModel : ViewModel() {
    private val activityApiService = ActivityNetworkModule.activityApiService
    private val authPrefs: AuthSharedPreferencesEditor = AuthSharedPreferencesProvider.editor
        ?: throw IllegalStateException("AuthSharedPreferencesEditor not initialized yet")
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val TAG = "ActivityViewModel"

    internal val _uiState = MutableStateFlow(ActivityUiState())
    val uiState: StateFlow<ActivityUiState> = _uiState.asStateFlow()

    private var currentWeekStart: LocalDate = LocalDate.now().with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1)
    private var currentWeekEnd: LocalDate = currentWeekStart.plusDays(6)

    init {
        loadActivitiesForWeek(currentWeekStart, currentWeekEnd)
    }

    fun loadActivitiesForWeek(startDate: LocalDate, endDate: LocalDate) {
        // Если запрашиваемая неделя уже загружена, не делаем новый запрос
        if (startDate == currentWeekStart && endDate == currentWeekEnd && _uiState.value.weekActivities.isNotEmpty()) {
            return
        }

        currentWeekStart = startDate
        currentWeekEnd = endDate

        viewModelScope.launch {
            Log.d(TAG, "Загрузка тренировок за неделю: $startDate - $endDate")
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val token = authPrefs.jwt ?: throw IllegalStateException("Токен авторизации не найден")
                val response = activityApiService.getActivitiesByWeek(
                    token,
                    startDate.format(dateFormatter),
                    endDate.format(dateFormatter)
                )
                
                if (response.isSuccessful) {
                    val activities = response.body()?.activities ?: emptyList()
                    Log.d(TAG, "Получено тренировок: ${activities.size}")
                    
                    val weekActivities = mutableMapOf<LocalDate, MutableList<Workout>>()
                    
                    // Инициализируем пустые списки для всех дней недели
                    var currentDate = startDate
                    while (currentDate <= endDate) {
                        weekActivities[currentDate] = mutableListOf()
                        currentDate = currentDate.plusDays(1)
                    }
                    
                    activities.forEach { activity ->
                        try {
                            val activityDateTime = LocalDateTime.parse(activity.datetime, dateTimeFormatter)
                            val activityDate = activityDateTime.toLocalDate()
                            
                            if (activityDate in startDate..endDate) {
                                val workout = Workout(
                                    time = activity.datetime,
                                    name = activity.name,
                                    calories = activity.calories,
                                    intensityZones = activity.intensityZones
                                )
                                weekActivities[activityDate]?.add(workout)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Ошибка парсинга даты: ${activity.datetime}", e)
                        }
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        weekActivities = weekActivities,
                        currentWeekStart = startDate,
                        currentWeekEnd = endDate,
                        isLoading = false,
                        error = null
                    )
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

    fun loadActivitiesForDate(date: LocalDate) {
        // Проверяем, находится ли дата в текущей загруженной неделе
        if (date in currentWeekStart..currentWeekEnd) {
            val workouts = _uiState.value.weekActivities[date] ?: emptyList()
            val activityDay = ActivityDay(date = date, workouts = workouts)
            _uiState.value = _uiState.value.copy(
                selectedDate = date,
                currentActivityDay = activityDay
            )
        } else {
            // Если дата вне текущей недели, загружаем новую неделю
            val weekStart = date.with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1)
            val weekEnd = weekStart.plusDays(6)
            loadActivitiesForWeek(weekStart, weekEnd)
        }
    }

    fun addNewWorkout(workout: Workout) {
        viewModelScope.launch {
            Log.d(TAG, "Добавление новой тренировки: ${workout.name}, время: ${workout.time}")
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val token = authPrefs.jwt ?: throw IllegalStateException("Токен авторизации не найден")
                val request = ActivityRequest(
                    name = workout.name,
                    datetime = workout.time,
                    calories = workout.calories,
                    intensityZones = workout.intensityZones
                )
                
                val response = activityApiService.createActivity(token, request)
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

    fun setNewWorkoutData(
        name: String,
        date: LocalDate,
        time: LocalTime,
        duration: Int,
        effort: String
    ) {
        viewModelScope.launch {
            Log.d(TAG, "Добавление новой тренировки: name=$name, date=$date, time=$time, duration=$duration")
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                val token = authPrefs.jwt ?: throw IllegalStateException("Токен авторизации не найден")
                // Генерируем распределение времени по зонам интенсивности
                val intensityZones = generateIntensityZones(duration, effort)
                
                // Комбинируем дату и время
                val dateTime = LocalDateTime.of(date, time)
                val formattedDateTime = dateTime.format(dateTimeFormatter)
                
                // Создаем запрос
                val request = ActivityRequest(
                    name = name,
                    datetime = formattedDateTime,
                    calories = calculateCaloriesFromZones(intensityZones),
                    intensityZones = intensityZones
                )
                
                val response = activityApiService.createActivity(token, request)
                if (response.isSuccessful) {
                    Log.d(TAG, "Тренировка успешно создана")
                    loadActivitiesForDate(date)
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
}

data class ActivityDay(
    val date: LocalDate,
    val workouts: List<Workout>
)

data class Workout(
    val time: String,
    val name: String,
    val calories: Int,
    val intensityZones: List<Int>
)

data class ActivitySummary(
    val calories: Int
)

/**
 * Рассчитывает калории на основе времени в каждой зоне интенсивности
 * @param intensityZones список времени в минутах для каждой зоны
 * @return общее количество сожженных калорий
 */
private fun calculateCaloriesFromZones(intensityZones: List<Int>): Int {
    // Базовые коэффициенты расхода калорий для каждой зоны (ккал/мин)
    val zoneCalorieRates = listOf(
        3.0,  // Очень легкая зона
        5.0,  // Легкая зона
        7.0,  // Умеренная зона
        9.0,  // Высокая зона
        12.0  // Максимальная зона
    )
    
    return intensityZones.mapIndexed { index, minutes ->
        (minutes * zoneCalorieRates[index]).toInt()
    }.sum()
}

/**
 * Генерирует распределение времени по зонам интенсивности
 * @param duration общая длительность тренировки в минутах
 * @param effort уровень усилий (Easy, Moderate, Hard, Very Hard, Maximum)
 * @return список из 5 чисел, представляющих время в каждой зоне
 */
private fun generateIntensityZones(duration: Int, effort: String): List<Int> {
    val zones = mutableListOf<Int>()
    var remainingTime = duration
    
    // Базовые веса для каждой зоны в зависимости от уровня усилий
    val zoneWeights = when (effort) {
        "Easy" -> listOf(0.4, 0.3, 0.2, 0.1, 0.0)
        "Moderate" -> listOf(0.2, 0.3, 0.3, 0.15, 0.05)
        "Hard" -> listOf(0.1, 0.2, 0.3, 0.25, 0.15)
        "Very Hard" -> listOf(0.05, 0.15, 0.25, 0.3, 0.25)
        "Maximum" -> listOf(0.0, 0.1, 0.2, 0.3, 0.4)
        else -> listOf(0.2, 0.2, 0.2, 0.2, 0.2)
    }
    
    // Генерируем время для каждой зоны
    for (i in 0..3) { // Для первых 4 зон
        val weight = zoneWeights[i]
        val zoneTime = (duration * weight).toInt()
        zones.add(zoneTime)
        remainingTime -= zoneTime
    }
    
    // Оставшееся время идет в последнюю зону
    zones.add(remainingTime)
    
    return zones
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

