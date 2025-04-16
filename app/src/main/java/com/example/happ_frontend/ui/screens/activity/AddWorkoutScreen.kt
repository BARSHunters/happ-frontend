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

    var showWorkoutTypeDialog by remember { mutableStateOf(false) }
    var showDateDialog by remember { mutableStateOf(false) }
    var showDurationDialog by remember { mutableStateOf(false) }
    var showEffortDialog by remember { mutableStateOf(false) }

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
                // Header with title and back button
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
                        text = "Details Workout",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF7B639C), // Purple color to match the design
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Workout Type Selector
                WorkoutTypeSelector(
                    selectedType = selectedWorkoutType,
                    onTypeSelected = { selectedWorkoutType = it },
                    onClick = { showWorkoutTypeDialog = true }
                )

                // Workout Date Selector
                WorkoutDateSelector(
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    onClick = { showDateDialog = true }
                )

                // Workout Duration Selector
                WorkoutDurationSelector(
                    duration = workoutDuration,
                    onDurationChanged = { workoutDuration = it },
                    onClick = { showDurationDialog = true }
                )

                // Workout Effort Selector
                WorkoutEffortSelector(
                    effort = workoutEffort,
                    onEffortChanged = { workoutEffort = it },
                    onClick = { showEffortDialog = true }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Add Workout Button
                Button(
                    onClick = {
                        viewModel.setNewWorkoutData(
                            type = selectedWorkoutType,
                            date = selectedDate,
                            duration = workoutDuration,
                            effort = workoutEffort
                        )
                        onSaveClick()
                    },
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

    // Workout Type Selection Dialog
    if (showWorkoutTypeDialog) {
        WorkoutTypeSelectionDialog(
            onDismiss = { showWorkoutTypeDialog = false },
            onTypeSelected = {
                selectedWorkoutType = it
                showWorkoutTypeDialog = false
            },
            currentSelection = selectedWorkoutType
        )
    }

    // Date Selection Dialog
    if (showDateDialog) {
        DateSelectionDialog(
            onDismiss = { showDateDialog = false },
            onDateSelected = {
                selectedDate = it
                showDateDialog = false
            },
            currentDate = selectedDate
        )
    }

    // Duration Selection Dialog
    if (showDurationDialog) {
        DurationSelectionDialog(
            onDismiss = { showDurationDialog = false },
            onDurationSelected = {
                workoutDuration = it
                showDurationDialog = false
            },
            currentDuration = workoutDuration
        )
    }

    // Effort Selection Dialog
    if (showEffortDialog) {
        EffortSelectionDialog(
            onDismiss = { showEffortDialog = false },
            onEffortSelected = {
                workoutEffort = it
                showEffortDialog = false
            },
            currentEffort = workoutEffort
        )
    }
}

@Composable
fun WorkoutTypeSelector(
    selectedType: WorkoutType,
    onTypeSelected: (WorkoutType) -> Unit,
    onClick: () -> Unit
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
        value = getWorkoutTypeDisplayName(selectedType),
        onClick = onClick
    )
}

@Composable
fun WorkoutDateSelector(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onClick: () -> Unit
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
        onClick = onClick
    )
}

@Composable
fun WorkoutDurationSelector(
    duration: Int,
    onDurationChanged: (Int) -> Unit,
    onClick: () -> Unit
) {
    val displayValue = if (duration >= 60) {
        val hours = duration / 60
        val minutes = duration % 60
        if (minutes == 0) "$hours hour${if (hours > 1) "s" else ""}" else "$hours h $minutes min"
    } else {
        "$duration min"
    }

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
        value = displayValue,
        onClick = onClick
    )
}

@Composable
fun WorkoutEffortSelector(
    effort: String,
    onEffortChanged: (String) -> Unit,
    onClick: () -> Unit
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
        onClick = onClick
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

@Composable
fun WorkoutTypeSelectionDialog(
    onDismiss: () -> Unit,
    onTypeSelected: (WorkoutType) -> Unit,
    currentSelection: WorkoutType
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Select Workout Type",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                WorkoutType.values().forEach { type ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onTypeSelected(type) }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = type == currentSelection,
                            onClick = { onTypeSelected(type) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFFA590B6)
                            )
                        )
                        Text(
                            text = getWorkoutTypeDisplayName(type),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA590B6)
                    )
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}

@Composable
fun DateSelectionDialog(
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    currentDate: LocalDate
) {
    val today = LocalDate.now()
    val dates = (-7..7).map { today.plusDays(it.toLong()) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Select Date",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                dates.forEach { date ->
                    val dateText = when {
                        date.isEqual(today) -> "Today"
                        date.isEqual(today.plusDays(1)) -> "Tomorrow"
                        date.isEqual(today.minusDays(1)) -> "Yesterday"
                        else -> date.format(DateTimeFormatter.ofPattern("EEE, dd MMM"))
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onDateSelected(date) }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = date.isEqual(currentDate),
                            onClick = { onDateSelected(date) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFFA590B6)
                            )
                        )
                        Text(
                            text = dateText,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA590B6)
                    )
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}

@Composable
fun DurationSelectionDialog(
    onDismiss: () -> Unit,
    onDurationSelected: (Int) -> Unit,
    currentDuration: Int
) {
    val durations = listOf(15, 30, 45, 60, 90, 120, 180)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Select Duration",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                durations.forEach { duration ->
                    val displayValue = if (duration >= 60) {
                        val hours = duration / 60
                        val minutes = duration % 60
                        if (minutes == 0) "$hours hour${if (hours > 1) "s" else ""}" else "$hours h $minutes min"
                    } else {
                        "$duration min"
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onDurationSelected(duration) }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = duration == currentDuration,
                            onClick = { onDurationSelected(duration) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFFA590B6)
                            )
                        )
                        Text(
                            text = displayValue,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA590B6)
                    )
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}

@Composable
fun EffortSelectionDialog(
    onDismiss: () -> Unit,
    onEffortSelected: (String) -> Unit,
    currentEffort: String
) {
    val effortLevels = listOf("Easy", "Moderate", "Hard", "Very Hard", "Maximum")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Select Effort Level",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                effortLevels.forEach { effort ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onEffortSelected(effort) }
                            .padding(vertical = 12.dp, horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = effort == currentEffort,
                            onClick = { onEffortSelected(effort) },
                            colors = RadioButtonDefaults.colors(
                                selectedColor = Color(0xFFA590B6)
                            )
                        )
                        Text(
                            text = effort,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA590B6)
                    )
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}

// Helper function to get user-friendly workout type names
fun getWorkoutTypeDisplayName(type: WorkoutType): String {
    return when (type) {
        WorkoutType.STRENGTH -> "Strength Training"
        WorkoutType.HIIT -> "HIIT"
        WorkoutType.YOGA -> "Yoga"
        WorkoutType.PILATES -> "Pilates"
        WorkoutType.SWIMMING -> "Swimming"
        WorkoutType.CYCLING -> "Cycling"
        WorkoutType.RUNNING -> "Running"
        else -> type.name.lowercase().capitalize()
    }
}

// Extension function to update ActivityViewModel with new workout data
fun ActivityViewModel.setNewWorkoutData(
    type: WorkoutType,
    date: LocalDate,
    duration: Int,
    effort: String
) {
    // This is where you would update the ViewModel with the new workout data
    // Implementation will depend on how your ViewModel is structured
    // For now, this is just a placeholder
}