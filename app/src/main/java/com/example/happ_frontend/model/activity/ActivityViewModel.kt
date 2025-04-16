
package com.example.happ_frontend.model.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class ActivityViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ActivityUiState())
    val uiState: StateFlow<ActivityUiState> = _uiState.asStateFlow()

    init {
        loadActivitiesForDate(LocalDate.now())
    }

    fun loadActivitiesForDate(date: LocalDate) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // TODO: Call API to get activities for the selected date
            // Example API call - implement actual API client later
            // val response = api.getActivities(date.format(DateTimeFormatter.ISO_DATE))

            // Mock data based on the date
            val workouts = if (date == LocalDate.now()) {
                // Today's workouts
                listOf(
                    Workout(
                        time = "10:00 am",
                        name = "Breathing Exercise",
                        duration = 15,
                        calories = 10,
                        heartRateAvg = 75,
                        heartRateMax = 85,
                        activityZones = listOf(8, 4, 2, 1, 0),
                        trainingLoad = 25,
                        recoveryTime = 1,
                        workoutType = WorkoutType.BREATHING
                    ),
                    Workout(
                        time = "08:00 am",
                        name = "Pilates",
                        duration = 60,
                        calories = 180,
                        heartRateAvg = 110,
                        heartRateMax = 145,
                        activityZones = listOf(5, 20, 25, 8, 2),
                        trainingLoad = 65,
                        recoveryTime = 8,
                        workoutType = WorkoutType.PILATES
                    )
                )
            } else if (date.isBefore(LocalDate.now())) {
                // Past workouts
                listOf(
                    Workout(
                        time = "07:30 am",
                        name = "Morning Run",
                        duration = 30,
                        calories = 320,
                        heartRateAvg = 145,
                        heartRateMax = 175,
                        activityZones = listOf(2, 5, 15, 6, 2),
                        trainingLoad = 78,
                        recoveryTime = 12,
                        workoutType = WorkoutType.RUNNING
                    ),
                    Workout(
                        time = "06:00 pm",
                        name = "Yoga Session",
                        duration = 45,
                        calories = 130,
                        heartRateAvg = 90,
                        heartRateMax = 110,
                        activityZones = listOf(10, 20, 10, 5, 0),
                        trainingLoad = 35,
                        recoveryTime = 4,
                        workoutType = WorkoutType.YOGA
                    )
                )
            } else {
                // Future - empty list since workouts haven't been recorded yet
                emptyList()
            }

            val activityDay = ActivityDay(date = date, workouts = workouts)

            // Update UI state with the fetched or mock data
            _uiState.value = _uiState.value.copy(
                selectedDate = date,
                currentActivityDay = activityDay,
                isLoading = false
            )
        }
    }

    fun addNewWorkout() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Switch to today's date
            val today = LocalDate.now()

            // TODO: Call API to record a new workout
            // Example API call - implement actual API client later
            // val response = api.newActivity(today.format(DateTimeFormatter.ISO_DATE))

            // Mock data for a newly created workout
            val newWorkout = Workout(
                time = "10:00 am",
                name = "HIIT Training",
                duration = 25,
                calories = 280,
                heartRateAvg = 150,
                heartRateMax = 180,
                activityZones = listOf(2, 5, 8, 7, 3),
                trainingLoad = 85,
                recoveryTime = 14,
                workoutType = WorkoutType.HIIT
            )

            // Get current workouts or create empty list
            val currentWorkouts = _uiState.value.currentActivityDay?.workouts ?: emptyList()
            // Add new workout to the beginning of the list
            val updatedWorkouts = listOf(newWorkout) + currentWorkouts
            val activityDay = ActivityDay(date = today, workouts = updatedWorkouts)

            // After API call, update UI state with the new workout added
            _uiState.value = _uiState.value.copy(
                selectedDate = today,
                currentActivityDay = activityDay,
                isLoading = false
            )
        }
    }

    fun getActivitySummary(workouts: List<Workout>): ActivitySummary {
        var totalCalories = 0
        var totalDuration = 0
        var maxHeartRate = 0
        var avgHeartRate = 0
        var totalTrainingLoad = 0
        var maxRecoveryTime = 0

        workouts.forEach { workout ->
            totalCalories += workout.calories
            totalDuration += workout.duration
            if (workout.heartRateMax > maxHeartRate) maxHeartRate = workout.heartRateMax
            avgHeartRate += workout.heartRateAvg
            totalTrainingLoad += workout.trainingLoad
            if (workout.recoveryTime > maxRecoveryTime) maxRecoveryTime = workout.recoveryTime
        }

        if (workouts.isNotEmpty()) {
            avgHeartRate /= workouts.size
        }

        return ActivitySummary(
            calories = totalCalories,
            duration = totalDuration,
            heartRateMax = maxHeartRate,
            heartRateAvg = avgHeartRate,
            trainingLoad = totalTrainingLoad,
            recoveryTime = maxRecoveryTime
        )
    }
}

data class ActivityUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val currentActivityDay: ActivityDay? = null,
    val activityHistory: List<ActivityDay> = emptyList(),
    val isLoading: Boolean = false
)

data class ActivityDay(
    val date: LocalDate,
    val workouts: List<Workout>
)

enum class WorkoutType {
    RUNNING,
    WALKING,
    CYCLING,
    SWIMMING,
    YOGA,
    PILATES,
    HIIT,
    STRENGTH,
    BREATHING,
    OTHER
}

data class Workout(
    val time: String,
    val name: String,
    val duration: Int, // in minutes
    val calories: Int,
    val heartRateAvg: Int,
    val heartRateMax: Int,
    val activityZones: List<Int>, // Time in each of the 5 zones (in minutes)
    val trainingLoad: Int,
    val recoveryTime: Int, // in hours
    val workoutType: WorkoutType = WorkoutType.OTHER
)

data class ActivitySummary(
    val calories: Int,
    val duration: Int,
    val heartRateMax: Int,
    val heartRateAvg: Int,
    val trainingLoad: Int,
    val recoveryTime: Int
)



/**
 * Updates the data for a new workout being created
 */
fun ActivityViewModel.setNewWorkoutData(
    type: WorkoutType,
    date: LocalDate,
    duration: Int,
    effort: String
) {
    // Store this information in the ViewModel
    // This is a mock implementation - modify according to your actual ViewModel structure

    // Create a mock workout based on the provided data
    val newWorkout = Workout(
        name = "${type.name.lowercase().capitalize()} Workout",
        time = "12:00 PM", // Mock time
        duration = duration,
        calories = calculateEstimatedCalories(type, duration, effort),
        workoutType = type,
        activityZones = generateMockActivityZones(effort),
        heartRateAvg = calculateEstimatedHeartRate(type, effort),
        heartRateMax = calculateEstimatedMaxHeartRate(type, effort),
        trainingLoad = TODO(),
        recoveryTime = TODO()
    )

    // Store this in the ViewModel (implementation will depend on your ViewModel structure)
    // For now, this is just a placeholder
    // viewModel.tempNewWorkout = newWorkout
}

// Helper functions for mock data generation

/**
 * Estimates calories burned based on workout type, duration and effort
 */
private fun calculateEstimatedCalories(type: WorkoutType, duration: Int, effort: String): Int {
    val baseCaloriesPerMinute = when (type) {
        WorkoutType.RUNNING -> 10
        WorkoutType.HIIT -> 12
        WorkoutType.STRENGTH -> 8
        WorkoutType.SWIMMING, WorkoutType.CYCLING -> 9
        WorkoutType.YOGA, WorkoutType.PILATES -> 5
        else -> 7
    }

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
 * Estimates average heart rate based on workout type and effort
 */
private fun calculateEstimatedHeartRate(type: WorkoutType, effort: String): Int {
    val baseHeartRate = when (type) {
        WorkoutType.RUNNING, WorkoutType.HIIT -> 140
        WorkoutType.STRENGTH, WorkoutType.SWIMMING, WorkoutType.CYCLING -> 130
        WorkoutType.YOGA, WorkoutType.PILATES -> 110
        else -> 120
    }

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
 * Estimates max heart rate based on workout type and effort
 */
private fun calculateEstimatedMaxHeartRate(type: WorkoutType, effort: String): Int {
    val avgHeartRate = calculateEstimatedHeartRate(type, effort)

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

