package com.example.happ_frontend.ui.screens.activity

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.happ_frontend.R
import com.example.happ_frontend.model.activity.ActivityViewModel
import com.example.happ_frontend.model.activity.Workout
import com.example.happ_frontend.ui.screens.weight.PageHeaderWithBackButton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun ActivityScreen(
    viewModel: ActivityViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var screenState by remember { mutableStateOf<ActivityScreenState>(ActivityScreenState.Main) }
    val TAG = "ActivityScreen"

    // Показываем ошибку, если она есть
    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            Log.e(TAG, "Ошибка: ${uiState.error}")
        }
    }

    // Only show the current screen based on state
    when (val currentState = screenState) {
        is ActivityScreenState.Main -> {
            MainActivityScreen(
                viewModel = viewModel,
                onBackClick = onBackClick,
                onAddWorkoutClick = { screenState = ActivityScreenState.AddWorkout },
                onWorkoutDetailClick = { screenState = ActivityScreenState.Detail }
            )
        }
        is ActivityScreenState.Detail -> {
            ActivityDetailScreen(
                onBackClick = { screenState = ActivityScreenState.Main },
                workouts = uiState.currentActivityDay?.workouts ?: emptyList(),
                activitySummary = viewModel.getActivitySummary(uiState.currentActivityDay?.workouts ?: emptyList())
            )
        }
        is ActivityScreenState.AddWorkout -> {
            AddWorkoutScreen(
                viewModel = viewModel,
                onBackClick = { screenState = ActivityScreenState.Main },
                onSaveClick = { name, date, time, duration ->
                    viewModel.addActivity(name, date, time, duration)
                    screenState = ActivityScreenState.Main
                }
            )
        }
    }
}

@Composable
fun MainActivityScreen(
    viewModel: ActivityViewModel,
    onBackClick: () -> Unit,
    onAddWorkoutClick: () -> Unit,
    onWorkoutDetailClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val TAG = "ActivityScreen"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = PaddingValues(
                start = 32.dp,
                end = 32.dp,
                top = 32.dp
            )),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        horizontalAlignment = Alignment.Start
    ) {
        // Header with back button and title
        PageHeaderWithBackButton(
            title = stringResource(R.string.health_category_activity_page_title),
            onGoBack = onBackClick
        )

        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            // Banner with activity information
            ActivityHeader(onDetailClick = {
                if (uiState.currentActivityDay?.workouts?.isNotEmpty() == true) {
                    onWorkoutDetailClick()
                }
            })

            // Calendar to select dates
            ActivityHistoryCalendar(
                selectedDate = uiState.selectedDate,
                onDateSelected = { date ->
                    Log.d(TAG, "Выбрана дата: $date")
                    viewModel.loadActivitiesForDate(date)
                }
            )

            // Workout details for the selected date
            ActivityHistoryDetails(activityDay = uiState.currentActivityDay)

            Spacer(modifier = Modifier.weight(1f))

            // Button to add a new workout
            AddWorkoutButton(onClick = onAddWorkoutClick)
        }

        // Loading indicator
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Color(0xFFA590B6) // Purple color to match the design
                )
            }
        }

        // Error message
        if (uiState.error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.error!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

sealed class ActivityScreenState {
    object Main : ActivityScreenState()
    object Detail : ActivityScreenState()
    object AddWorkout : ActivityScreenState()
}

private fun calculateCalories(duration: Int, effort: String): Int {
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