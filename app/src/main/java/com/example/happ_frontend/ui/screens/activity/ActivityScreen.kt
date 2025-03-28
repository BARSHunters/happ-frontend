package com.example.happ_frontend.ui.screens.activity

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.happ_frontend.model.activity.ActivityViewModel

@Composable
fun ActivityScreen(
    viewModel: ActivityViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var screenState by remember { mutableStateOf<ActivityScreenState>(ActivityScreenState.Main) }

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
                onSaveClick = {
                    viewModel.addNewWorkout()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header with back button and centered title
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFFA590B6) // Purple color to match the design
                )
            }

            Text(
                text = "Activity",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFF7B639C), // Purple color to match the design
                modifier = Modifier.align(Alignment.Center)
            )
        }

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
                viewModel.loadActivitiesForDate(date)
            }
        )

        // Workout details for the selected date
        ActivityHistoryDetails(activityDay = uiState.currentActivityDay)

        Spacer(modifier = Modifier.weight(1f))

        // Button to add a new workout
        AddWorkoutButton(onClick = onAddWorkoutClick)

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
    }
}

sealed class ActivityScreenState {
    object Main : ActivityScreenState()
    object Detail : ActivityScreenState()
    object AddWorkout : ActivityScreenState()
}