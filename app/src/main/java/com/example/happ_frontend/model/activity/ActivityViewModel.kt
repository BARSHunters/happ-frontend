package com.example.happ_frontend.model.activity

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.happ_frontend.model.activity.communication.ActivityNetworkModule
import com.example.happ_frontend.model.activity.request.ActivityRequest
import com.example.happ_frontend.model.activity.response.ActivityDTO
import com.example.happ_frontend.model.activity.response.HeartRate
import com.example.happ_frontend.model.activity.response.TrainingData
import com.example.happ_frontend.model.login_register.data.AuthSharedPreferencesEditor
import com.example.happ_frontend.model.login_register.data.AuthSharedPreferencesProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale
import kotlin.random.Random

class ActivityViewModel : ViewModel() {
    private val activityApiService = ActivityNetworkModule.activityApiService
    private val authPrefs: AuthSharedPreferencesEditor = AuthSharedPreferencesProvider.editor
        ?: throw IllegalStateException("AuthSharedPreferencesEditor not initialized yet")
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val TAG = "ActivityViewModel"

    internal val _uiState = MutableStateFlow(ActivityUiState())
    val uiState: StateFlow<ActivityUiState> = _uiState.asStateFlow()

    private var currentWeekStart: LocalDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
    private var currentWeekEnd: LocalDate = currentWeekStart.plusDays(6)

    init {
        loadActivitiesForWeek(currentWeekStart, currentWeekEnd)
    }

    fun loadActivitiesForWeek(startDate: LocalDate, endDate: LocalDate) {
        if (startDate == currentWeekStart && endDate == currentWeekEnd && _uiState.value.weekActivities.isNotEmpty()) {
            return
        }

        currentWeekStart = startDate
        currentWeekEnd = endDate

        viewModelScope.launch {
            Log.d(TAG, "Загрузка тренировок за неделю: $startDate - $endDate")
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val token = "Bearer ${authPrefs.jwt ?: throw IllegalStateException("Токен авторизации не найден")}"
                
                // Форматируем даты в формат yyyy-MM-dd для query-параметров
                val startDateStr = startDate.format(dateFormatter)
                val endDateStr = endDate.format(dateFormatter)
                
                val response = activityApiService.getActivities(
                    token,
                    startDateStr,
                    endDateStr
                )
                
                if (response.isSuccessful) {
                    val activities = response.body() ?: emptyList()
                    Log.d(TAG, "Получено тренировок: ${activities.size}")
                    
                    val weekActivities = mutableMapOf<LocalDate, MutableList<Workout>>()
                    
                    var currentDate = startDate
                    while (currentDate <= endDate) {
                        weekActivities[currentDate] = mutableListOf()
                        currentDate = currentDate.plusDays(1)
                    }
                    
                    activities.forEach { activity ->
                        try {
                            val activityDateTime = LocalDateTime.parse(activity.trainingDate, dateTimeFormatter)
                            val activityDate = activityDateTime.toLocalDate()
                            
                            if (activityDate in startDate..endDate) {
                                val workout = Workout(
                                    time = activity.trainingDate,
                                    name = activity.trainingName,
                                    calories = activity.caloriesBurned.toInt(),
                                    intensityZones = activity.intensityZones,
                                    avgHeartRate = activity.avgHeartRate,
                                    maxHeartRate = activity.maxHeartRate,
                                    met = activity.met,
                                    recoveryTime = activity.recoveryTime
                                )
                                weekActivities[activityDate]?.add(workout)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Ошибка парсинга даты: ${activity.trainingDate}", e)
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
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Ошибка загрузки тренировок: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при загрузке тренировок", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Ошибка при загрузке тренировок: ${e.message}"
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
            val weekStart = date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
            val weekEnd = weekStart.plusDays(6)
            // Обновляем выбранную дату перед загрузкой новой недели
            _uiState.value = _uiState.value.copy(selectedDate = date)
            loadActivitiesForWeek(weekStart, weekEnd)
        }
    }

    fun getActivitySummary(workouts: List<Workout>): ActivitySummary {
        var totalCalories = 0
        var totalMet = 0.0
        var totalAvgHeartRate = 0.0
        var maxHeartRate = 0
        var totalDuration = 0
        
        workouts.forEach { workout ->
            totalCalories += workout.calories
            totalMet += workout.met
            totalAvgHeartRate += workout.avgHeartRate
            if (workout.maxHeartRate > maxHeartRate) {
                maxHeartRate = workout.maxHeartRate
            }
            // Примерно оцениваем длительность на основе калорий и MET
            totalDuration += (workout.calories / (workout.met * 3.5)).toInt()
        }
        
        val avgMet = if (workouts.isNotEmpty()) totalMet / workouts.size else 0.0
        val avgHeartRate = if (workouts.isNotEmpty()) totalAvgHeartRate / workouts.size else 0.0
        
        return ActivitySummary(
            calories = totalCalories,
            met = avgMet,
            avgHeartRate = avgHeartRate,
            maxHeartRate = maxHeartRate,
            totalDuration = totalDuration
        )
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
                val token = "Bearer ${authPrefs.jwt ?: throw IllegalStateException("Токен авторизации не найден")}"
                // Генерируем распределение времени по зонам интенсивности
                val intensityZones = generateIntensityZones(duration, effort)
                
                // Комбинируем дату и время
                val dateTime = LocalDateTime.of(date, time)
                val formattedDateTime = dateTime.format(dateTimeFormatter)
                
                // Генерируем данные о пульсе
                val heartRates = generateHeartRates(dateTime, duration)
                val avgHeartRate = heartRates.map { it.heartRate }.average()
                val maxHeartRate = heartRates.maxOf { it.heartRate }
                
                // Рассчитываем MET на основе усилий
                val met = when (effort) {
                    "Easy" -> 3.0
                    "Moderate" -> 5.0
                    "Hard" -> 7.0
                    "Very Hard" -> 9.0
                    "Maximum" -> 12.0
                    else -> 5.0
                }
                
                // Рассчитываем время восстановления (примерно 1 минута на каждые 10 минут тренировки)
                val recoveryTime = (duration / 10).coerceAtLeast(5)
                
                // Создаем запрос
                val activityDTO = ActivityDTO(
                    duration = String.format("%02d:%02d:00", duration / 60, duration % 60),
                    name = name,
                    datetime = dateTime.toString(),
                    heartRates = heartRates
                )
                
                val response = activityApiService.addActivity(token, activityDTO)
                if (response.isSuccessful) {
                    Log.d(TAG, "Тренировка успешно создана")
                    
                    // Создаем новую тренировку
                    val newWorkout = Workout(
                        time = formattedDateTime,
                        name = name,
                        calories = calculateCaloriesFromZones(intensityZones),
                        intensityZones = intensityZones,
                        avgHeartRate = avgHeartRate,
                        maxHeartRate = maxHeartRate,
                        met = met,
                        recoveryTime = recoveryTime
                    )
                    
                    // Получаем текущие тренировки для выбранной даты
                    val currentWorkouts = _uiState.value.weekActivities[date]?.toMutableList() ?: mutableListOf()
                    
                    // Добавляем новую тренировку
                    currentWorkouts.add(newWorkout)
                    
                    // Обновляем данные в состоянии
                    val updatedWeekActivities = _uiState.value.weekActivities.toMutableMap()
                    updatedWeekActivities[date] = currentWorkouts
                    
                    // Обновляем состояние
                    _uiState.value = _uiState.value.copy(
                        weekActivities = updatedWeekActivities,
                        selectedDate = date,
                        currentActivityDay = ActivityDay(date = date, workouts = currentWorkouts),
                        isLoading = false
                    )
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

    fun addActivity(name: String, date: LocalDate, time: LocalTime, durationMinutes: Int) {
        viewModelScope.launch {
            try {
                val token = "Bearer ${authPrefs.jwt ?: throw IllegalStateException("Токен авторизации не найден")}"
                
                val startDateTime = LocalDateTime.of(date, time)
                val heartRates = generateHeartRates(startDateTime, durationMinutes)
                
                val activityDTO = ActivityDTO(
                    duration = String.format("%02d:%02d:00", durationMinutes / 60, durationMinutes % 60),
                    name = name,
                    datetime = startDateTime.format(dateTimeFormatter),
                    heartRates = heartRates
                )
                
                val response = activityApiService.addActivity(token, activityDTO)
                
                if (response.isSuccessful) {
                    // Перезагружаем тренировки за день
                    loadActivitiesForDay(date)
                } else {
                    _uiState.value = _uiState.value.copy(
                        error = "Ошибка добавления тренировки: ${response.code()}"
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при добавлении тренировки", e)
                _uiState.value = _uiState.value.copy(
                    error = "Ошибка при добавлении тренировки: ${e.message}"
                )
            }
        }
    }

    private fun generateHeartRates(startDateTime: LocalDateTime, durationMinutes: Int): List<HeartRate> {
        val heartRates = mutableListOf<HeartRate>()
        val baseHeartRate = 120 // Базовый пульс
        val variation = 20 // Вариация пульса
        
        for (minute in 0 until durationMinutes) {
            val timestamp = startDateTime.plusMinutes(minute.toLong())
            val randomVariation = Random.nextInt(-variation, variation)
            val heartRate = baseHeartRate + randomVariation
            
            heartRates.add(HeartRate(
                timestamp = timestamp.toEpochSecond(java.time.ZoneOffset.UTC),
                heartRate = heartRate
            ))
        }
        
        return heartRates
    }

    private fun loadActivitiesForDay(date: LocalDate) {
        // Для загрузки тренировок за день используем тот же метод, но с одинаковыми датами начала и конца
        loadActivitiesForWeek(date, date)
    }
}

data class ActivityDay(
    val date: LocalDate,
    val workouts: List<Workout>
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
