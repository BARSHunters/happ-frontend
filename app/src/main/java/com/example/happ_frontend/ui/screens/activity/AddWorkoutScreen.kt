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
import androidx.compose.material.icons.filled.CalendarToday
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AddWorkoutScreen(
    viewModel: ActivityViewModel,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    var workoutName by remember { mutableStateOf("Тренировка") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var workoutDuration by remember { mutableStateOf(60) } // Default 1 hour
    var workoutEffort by remember { mutableStateOf("Hard") }

    var showDateDialog by remember { mutableStateOf(false) }
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
                        text = "Добавление тренировки",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF7B639C), // Purple color to match the design
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Workout Name TextField
                OutlinedTextField(
                    value = workoutName,
                    onValueChange = { workoutName = it },
                    label = { Text("Название тренировки") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFFA590B6),
                        focusedLabelColor = Color(0xFFA590B6)
                    )
                )

                // Workout Date Selector
                WorkoutDateSelector(
                    selectedDate = selectedDate,
                    onDateSelected = { selectedDate = it },
                    onClick = { showDateDialog = true }
                )

                // Workout Duration Slider
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Длительность: $workoutDuration минут",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    
                    Slider(
                        value = workoutDuration.toFloat(),
                        onValueChange = { value -> 
                            // Округляем до ближайшего кратного 5
                            val rounded = (value / 5).toInt() * 5
                            workoutDuration = rounded.coerceIn(5, 120)
                        },
                        valueRange = 5f..120f,
                        steps = (120 - 5) / 5 - 1, // Шаг 5 минут
                        modifier = Modifier.padding(horizontal = 8.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFFA590B6),
                            activeTrackColor = Color(0xFFA590B6)
                        )
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("5 мин", style = MaterialTheme.typography.bodySmall)
                        Text("120 мин", style = MaterialTheme.typography.bodySmall)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Add Workout Button
                Button(
                    onClick = {
                        viewModel.setNewWorkoutData(
                            name = workoutName,
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
                        text = "Добавить тренировку",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
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

    // Effort Selection Dialog - скрытый диалог для выбора интенсивности нагрузки
    // Теперь он открывается программно, а не через UI
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
fun WorkoutDateSelector(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
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
            Icon(
                imageVector = Icons.Default.CalendarToday,
                contentDescription = null,
                tint = Color(0xFFA590B6),
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
            )

            // Left side with title
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = "Дата тренировки",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                
                Text(
                    text = when {
                        selectedDate.isEqual(LocalDate.now()) -> "Сегодня"
                        selectedDate.isEqual(LocalDate.now().plusDays(1)) -> "Завтра"
                        selectedDate.isEqual(LocalDate.now().minusDays(1)) -> "Вчера"
                        else -> selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Right arrow with padding to ensure proper spacing
            Box(modifier = Modifier.width(40.dp), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = Color.Gray
                )
            }
        }
    }
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
        title = "Уровень интенсивности",
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
fun DateSelectionDialog(
    onDismiss: () -> Unit,
    onDateSelected: (LocalDate) -> Unit,
    currentDate: LocalDate
) {
    val today = LocalDate.now()
    
    // Создаем список дат для выбора (от -30 до +30 дней от текущей даты)
    val dates = (-30..30).map { today.plusDays(it.toLong()) }
    
    // Находим индекс текущей выбранной даты
    val initialSelectedIndex = dates.indexOfFirst { it.isEqual(currentDate) }.coerceAtLeast(0)
    
    var selectedIndex by remember { mutableStateOf(initialSelectedIndex) }

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
                    text = "Выберите дату",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Создаем DatePicker для более удобного выбора даты
                DatePickerSlider(
                    dates = dates,
                    selectedIndex = selectedIndex,
                    onSelectionChanged = { selectedIndex = it }
                )

                Button(
                    onClick = { 
                        onDateSelected(dates[selectedIndex])
                        onDismiss()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFA590B6)
                    )
                ) {
                    Text("Подтвердить")
                }
            }
        }
    }
}

@Composable
fun DatePickerSlider(
    dates: List<LocalDate>,
    selectedIndex: Int,
    onSelectionChanged: (Int) -> Unit
) {
    val today = LocalDate.now()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Текущая выбранная дата (большим шрифтом)
            Text(
                text = when {
                    dates[selectedIndex].isEqual(today) -> "Сегодня"
                    dates[selectedIndex].isEqual(today.plusDays(1)) -> "Завтра"
                    dates[selectedIndex].isEqual(today.minusDays(1)) -> "Вчера"
                    else -> ""
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            Text(
                text = dates[selectedIndex].format(DateTimeFormatter.ofPattern("dd MMMM yyyy")),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Day of week selection
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Previous button
                IconButton(
                    onClick = { if (selectedIndex > 0) onSelectionChanged(selectedIndex - 1) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Previous day",
                        tint = if (selectedIndex > 0) Color(0xFFA590B6) else Color.Gray.copy(alpha = 0.5f),
                        modifier = Modifier.size(24.dp)
                    )
                }
                
                // Days
                val visibleDays = 5 // Показываем 5 дней
                val startIdx = maxOf(0, selectedIndex - visibleDays / 2)
                val endIdx = minOf(dates.size - 1, startIdx + visibleDays - 1)
                
                for (i in startIdx..endIdx) {
                    DateDayButton(
                        date = dates[i],
                        isSelected = i == selectedIndex,
                        onClick = { onSelectionChanged(i) }
                    )
                }
                
                // Next button
                IconButton(
                    onClick = { if (selectedIndex < dates.size - 1) onSelectionChanged(selectedIndex + 1) },
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next day",
                        tint = if (selectedIndex < dates.size - 1) Color(0xFFA590B6) else Color.Gray.copy(alpha = 0.5f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DateDayButton(
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val today = LocalDate.now()
    val isToday = date.isEqual(today)
    
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(
                if (isSelected) Color(0xFFA590B6)
                else if (isToday) Color(0xFFE6E0F0)
                else Color.Transparent
            )
            .border(
                width = 1.dp,
                color = if (isToday && !isSelected) Color(0xFFA590B6) else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = if (isSelected) Color.White else if (isToday) Color(0xFFA590B6) else Color.Black,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal
        )
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
                    text = "Выберите уровень нагрузки",
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
                    Text("Подтвердить")
                }
            }
        }
    }
}

// Extension function to update ActivityViewModel with new workout data
fun ActivityViewModel.setNewWorkoutData(
    name: String,
    date: LocalDate,
    duration: Int,
    effort: String
) {
    // This is where you would update the ViewModel with the new workout data
    // Implementation will depend on how your ViewModel is structured
    // For now, this is just a placeholder
}