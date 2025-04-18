package com.example.happ_frontend.model.activity

import java.time.LocalDate
import java.time.temporal.WeekFields
import java.util.*

data class ActivityUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val currentActivityDay: ActivityDay = ActivityDay(date = LocalDate.now(), workouts = emptyList()),
    val activityHistory: List<ActivityDay> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val weekActivities: Map<LocalDate, List<Workout>> = emptyMap(),
    val currentWeekStart: LocalDate = LocalDate.now().with(WeekFields.of(Locale.getDefault()).dayOfWeek(), 1),
    val currentWeekEnd: LocalDate = currentWeekStart.plusDays(6)
) 