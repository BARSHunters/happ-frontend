package com.example.happ_frontend.ui.screens.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.happ_frontend.model.activity.ActivityViewModel
import com.example.happ_frontend.model.activity.WorkoutType
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AddWorkoutScreen(
    viewModel: ActivityViewModel,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    var selectedWorkoutType by remember { mutableStateOf(WorkoutType.STRENGTH) }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var workoutDuration by remember { mutableStateOf(60) } // Default 1 hour
    var workoutEffort by remember { mutableStateOf("Hard") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header with back button and title
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
                    tint = Color(0xFFA590B6)
                )
            }

            Text(
                text = "Activity",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFF7B639C),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Activity header banner (reused from main screen)
        ActivityHeader()

        // Activity History (reused from main screen but simplified)
        Text(
            text = "Activity History",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        // Simplified week view
        WeekDaySelector(selectedDate = selectedDate, onDateSelected = { selectedDate = it })

        // Workout workouts summary (similar to main screen)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = "${selectedDate.format(DateTimeFormatter.ofPattern("d MMM"))} - ${
                    selectedDate.dayOfWeek.name.lowercase().capitalize()
                }${if (selectedDate.isEqual(LocalDate.now())) " - Today" else ""}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            val uiState by viewModel.uiState.collectAsState()
            if (uiState.currentActivityDay?.workouts?.isNotEmpty() == true) {
                Spacer(modifier = Modifier.height(8.dp))
                uiState.currentActivityDay?.workouts?.forEach { workout ->
                    Text(
                        text = "${workout.time} - ${workout.duration} min of ${workout.name} (~${workout.calories} cal)",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Details Workout Section
        Text(
            text = "Details Workout",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )

        // Workout Type Selector
        WorkoutTypeSelector(
            selectedType = selectedWorkoutType,
            onTypeSelected = { selectedWorkoutType = it }
        )

        // Workout Date Selector
        WorkoutDateSelector(
            selectedDate = selectedDate,
            onDateSelected = { selectedDate = it }
        )

        // Workout Duration Selector
        WorkoutDurationSelector(
            duration = workoutDuration,
            onDurationChanged = { workoutDuration = it }
        )

        // Workout Effort Selector
        WorkoutEffortSelector(
            effort = workoutEffort,
            onEffortChanged = { workoutEffort = it }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Add Workout Button
        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(32.dp)),
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFA590B6)
            )
        ) {
            Text(
                text = "Add Workout",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}

@Composable
fun WorkoutTypeSelector(
    selectedType: WorkoutType,
    onTypeSelected: (WorkoutType) -> Unit
) {
    SettingItemWithNavigation(
        icon = {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color.Gray
            )
        },
        title = "Choose Workout",
        value = "Upperbody Workout",
        onClick = { /* Navigate to workout type selection screen */ }
    )
}

@Composable
fun WorkoutDateSelector(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    SettingItemWithNavigation(
        icon = {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color.Gray
            )
        },
        title = "Workout date",
        value = if (selectedDate.isEqual(LocalDate.now())) "Today" else selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
        onClick = { /* Navigate to date selection screen */ }
    )
}

@Composable
fun WorkoutDurationSelector(
    duration: Int,
    onDurationChanged: (Int) -> Unit
) {
    SettingItemWithNavigation(
        icon = {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color.Gray
            )
        },
        title = "Workout duration",
        value = "$duration hour",
        onClick = { /* Navigate to duration selection screen */ }
    )
}

@Composable
fun WorkoutEffortSelector(
    effort: String,
    onEffortChanged: (String) -> Unit
) {
    SettingItemWithNavigation(
        icon = {
            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = Color.Gray
            )
        },
        title = "Effort",
        value = effort,
        onClick = { /* Navigate to effort selection screen */ }
    )
}

@Composable
fun SettingItemWithNavigation(
    icon: @Composable () -> Unit,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFF1F5FB))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left icon or placeholder
            Icon(
                imageVector = when (title) {
                    "Choose Workout" -> Icons.Default.ArrowForward // Placeholder for workout icon
                    "Workout date" -> Icons.Default.ArrowForward // Placeholder for calendar icon
                    "Workout duration" -> Icons.AutoMirrored.Filled.ArrowForward // Placeholder for clock icon
                    else -> Icons.Default.ArrowForward // Placeholder for effort icon
                },
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Gray
            )

            // Title and value
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            // Value on the right
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp)
            )

            // Navigation icon
            icon()
        }
    }
}

@Composable
fun WeekDaySelector(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    // Find the Monday of the current week
    val monday = selectedDate.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))

    // Generate weekdays (Monday to Sunday)
    val weekdays = (0..6).map { monday.plusDays(it.toLong()) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        weekdays.forEach { date ->
            DayItem(
                date = date,
                isSelected = date.isEqual(selectedDate),
                onSelected = { onDateSelected(date) }
            )
        }
    }
}

@Composable
fun DayItem(
    date: LocalDate,
    isSelected: Boolean,
    onSelected: () -> Unit
) {
    val backgroundColor = if (isSelected)
        Color(0xFF9D89C5)
    else
        MaterialTheme.colorScheme.surface

    val textColor = if (isSelected)
        Color.White
    else
        MaterialTheme.colorScheme.onSurface

    val today = LocalDate.now()
    val isToday = date.equals(today)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(16.dp)
            )
            .width(44.dp)
            .height(60.dp)
            .clickable(onClick = onSelected)
            .padding(4.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = textColor
        )

        Text(
            text = date.dayOfWeek.name.take(3).uppercase(),
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}