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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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

    // Create a modal dialog with a white background
    Dialog(
        onDismissRequest = onBackClick,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)) // Darkened background
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header with title
                Text(
                    text = "Details Workout",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
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

                Spacer(modifier = Modifier.height(16.dp))

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
            // Left side with title
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