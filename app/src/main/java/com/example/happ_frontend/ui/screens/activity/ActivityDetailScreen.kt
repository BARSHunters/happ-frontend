package com.example.happ_frontend.ui.screens.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.happ_frontend.model.activity.ActivitySummary
import com.example.happ_frontend.model.activity.Workout

@Composable
fun ActivityDetailScreen(
    onBackClick: () -> Unit,
    workouts: List<Workout>,
    activitySummary: ActivitySummary
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
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
                text = "Workout Details",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFF7B639C),
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF6E9F8)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Summary",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SummaryItem(
                        label = "Duration",
                        value = "${activitySummary.duration} min"
                    )
                    SummaryItem(
                        label = "Calories",
                        value = "${activitySummary.calories} cal"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SummaryItem(
                        label = "Avg HR",
                        value = "${activitySummary.heartRateAvg} bpm"
                    )
                    SummaryItem(
                        label = "Max HR",
                        value = "${activitySummary.heartRateMax} bpm"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SummaryItem(
                        label = "Training Load",
                        value = "${activitySummary.trainingLoad}"
                    )
                    SummaryItem(
                        label = "Recovery",
                        value = "${activitySummary.recoveryTime} hrs"
                    )
                }
            }
        }

        // Activity Zones Chart
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Activity Zones",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (workouts.isNotEmpty()) {
                    val workout = workouts[0] // Using the first workout for the chart
                    ActivityZonesChart(activityZones = workout.activityZones)
                }
            }
        }

        // Heart Rate Chart
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Heart Rate",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                // Placeholder for heart rate chart
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF6E9F8)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Heart Rate Chart\nAvg: ${activitySummary.heartRateAvg} bpm\nMax: ${activitySummary.heartRateMax} bpm",
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Workout Details
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Workout Details",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                workouts.forEach { workout ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = workout.name,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Time: ${workout.time}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Duration: ${workout.duration} minutes",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Calories: ${workout.calories} cal",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Type: ${workout.workoutType.name.lowercase().capitalize()}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    if (workout != workouts.last()) {
                        Divider(
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun ActivityZonesChart(activityZones: List<Int>) {
    if (activityZones.size != 5) {
        Text(text = "Activity zones data unavailable")
        return
    }

    val totalTime = activityZones.sum().toFloat().coerceAtLeast(1f)
    val zoneColors = listOf(
        Color(0xFF8DD8F8), // Zone 1 - Light blue
        Color(0xFF41B6E6), // Zone 2 - Medium blue
        Color(0xFF00A1DF), // Zone 3 - Dark blue
        Color(0xFFFF9500), // Zone 4 - Orange
        Color(0xFFFF3B30)  // Zone 5 - Red
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Bar chart for zones
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(24.dp)
                .clip(RoundedCornerShape(4.dp))
        ) {
            for (i in activityZones.indices) {
                val width = (activityZones[i] / totalTime)
                if (width > 0) {
                    Box(
                        modifier = Modifier
                            .weight(width)
                            .fillMaxHeight()
                            .background(zoneColors[i])
                    )
                }
            }
        }

        // Legend
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            for (i in activityZones.indices) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(zoneColors[i], RoundedCornerShape(2.dp))
                    )
                    Text(
                        text = "Z${i + 1}",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "${activityZones[i]} min",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}