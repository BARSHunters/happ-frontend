package com.example.happ_frontend.ui.screens.activity

import AuthFormDatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.happ_frontend.R
import com.example.happ_frontend.model.activity.ActivityViewModel
import com.example.happ_frontend.ui.screens.login_register.AuthFormTimePicker
import java.time.LocalDate
import java.time.LocalTime

@Composable
fun AddWorkoutScreen(
    viewModel: ActivityViewModel,
    onBackClick: () -> Unit,
    onSaveClick: (String, LocalDate, LocalTime, Int) -> Unit
) {
    var workoutName by remember { mutableStateOf("") }
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var selectedTime by remember { mutableStateOf(LocalTime.now()) }
    var workoutDuration by remember { mutableStateOf(60) }

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
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Header
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
                            contentDescription = stringResource(R.string.dialog_misc_buttons_cancel),
                            tint = Color(0xFF7B639C)
                        )
                    }

                    Text(
                        text = stringResource(R.string.health_category_activity_dialog_add_title),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF7B639C),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Workout Name
                OutlinedTextField(
                    value = workoutName,
                    onValueChange = { workoutName = it },
                    label = { Text(stringResource(R.string.health_category_activity_dialog_name)) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF7B639C),
                        focusedLabelColor = Color(0xFF7B639C)
                    )
                )

                // Date and Time Selection
                AuthFormDatePicker(
                    labelText = stringResource(R.string.health_category_weight_dialog_field_date),
                    value = selectedDate,
                    onValueChange = { it?.let { newDate -> selectedDate = newDate } }
                )

                AuthFormTimePicker(
                    labelText = stringResource(R.string.health_category_weight_dialog_field_time),
                    value = selectedTime,
                    onValueChange = { it?.let { newTime -> selectedTime = newTime } }
                )

                // Duration Slider
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(
                            R.string.health_category_activity_dialog_duration_with_value,
                            workoutDuration
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                    
                    Slider(
                        value = workoutDuration.toFloat(),
                        onValueChange = { value -> 
                            val rounded = (value / 5).toInt() * 5
                            workoutDuration = rounded.coerceIn(5, 120)
                        },
                        valueRange = 5f..120f,
                        steps = (120 - 5) / 5 - 1,
                        modifier = Modifier.padding(horizontal = 8.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = Color(0xFF7B639C),
                            activeTrackColor = Color(0xFF7B639C)
                        )
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.health_category_activity_dialog_duration_min, 5),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            text = stringResource(R.string.health_category_activity_dialog_duration_min, 120),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                // Save Button
                Button(
                    onClick = {
                        onSaveClick(workoutName, selectedDate, selectedTime, workoutDuration)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(32.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF7B639C)
                    ),
                    enabled = workoutName.isNotBlank()
                ) {
                    Text(
                        text = stringResource(R.string.add),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

