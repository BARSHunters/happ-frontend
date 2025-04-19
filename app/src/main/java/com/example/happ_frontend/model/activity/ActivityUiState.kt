package com.example.happ_frontend.model.activity

import java.time.LocalDate
import java.time.DayOfWeek
import java.time.temporal.TemporalAdjusters
import java.util.*

data class ActivityUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val currentActivityDay: ActivityDay = ActivityDay(date = LocalDate.now(), workouts = emptyList()),
    val activityHistory: List<ActivityDay> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val weekActivities: Map<LocalDate, List<Workout>> = emptyMap(),
    val currentWeekStart: LocalDate = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)),
    val currentWeekEnd: LocalDate = currentWeekStart.plusDays(6)
) 