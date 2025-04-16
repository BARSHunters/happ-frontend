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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.happ_frontend.R
import com.example.happ_frontend.model.activity.ActivitySummary
import com.example.happ_frontend.model.activity.Workout
import com.example.happ_frontend.ui.screens.weight.PageHeaderWithBackButton

@Composable
fun ActivityDetailScreen(
    onBackClick: () -> Unit,
    workouts: List<Workout>,
    activitySummary: ActivitySummary
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = PaddingValues(
                start = 32.dp,
                end = 32.dp,
                top = 32.dp
            ))
    ) {
        // Header with back button and title
        PageHeaderWithBackButton(
            title = stringResource(R.string.health_category_activity_detail_title),
            onGoBack = onBackClick
        )

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            // Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF6F3FD) // Light purple background to match the weight design
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(R.string.health_category_activity_detail_summary),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF7B639C)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        SummaryItem(
                            label = stringResource(R.string.health_category_activity_detail_duration),
                            value = "${activitySummary.duration} min"
                        )
                        SummaryItem(
                            label = stringResource(R.string.health_category_activity_detail_calories),
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
                        text = stringResource(R.string.health_category_activity_detail_zones),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    if (workouts.isNotEmpty()) {
                        val workout = workouts[0] // Using the first workout for the chart
                        ActivityZonesChart(activityZones = convertToZonesPairs(workout.activityZones))
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
                        text = stringResource(R.string.health_category_activity_detail_heart_rate),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Placeholder for heart rate chart
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFF6F3FD)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Heart Rate Chart\nAvg: ${activitySummary.heartRateAvg} bpm\nMax: ${activitySummary.heartRateMax} bpm",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
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
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    // Display workout details
                    workouts.forEach { workout ->
                        WorkoutDetailItem(workout = workout)
                    }
                }
            }
        }
    }
}

// Функция для конвертации списка Int в список пар String, Float
fun convertToZonesPairs(activityZones: List<Int>): List<Pair<String, Float>> {
    val zoneNames = listOf("Easy", "Fat Burn", "Cardio", "Peak", "Anaerobic")
    val totalTime = activityZones.sum().toFloat().coerceAtLeast(1f)
    
    return activityZones.mapIndexed { index, time ->
        val zoneName = if (index < zoneNames.size) zoneNames[index] else "Zone ${index + 1}"
        val percentage = time / totalTime
        zoneName to percentage
    }
}

@Composable
fun SummaryItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF7B639C)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun ActivityZonesChart(activityZones: List<Pair<String, Float>>) {
    // Simple implementation - in a real app this would be a proper chart
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        activityZones.forEach { (zoneName, percentage) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = zoneName,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(80.dp)
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(16.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEEEEEE))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(percentage)
                            .background(getZoneColor(zoneName))
                    )
                }
                Text(
                    text = "${(percentage * 100).toInt()}%",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.width(40.dp),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

fun getZoneColor(zoneName: String): Color {
    return when(zoneName) {
        "Easy" -> Color(0xFF8BC34A)
        "Fat Burn" -> Color(0xFF4CAF50)
        "Cardio" -> Color(0xFFFF9800)
        "Peak" -> Color(0xFFE91E63)
        else -> Color(0xFF9C27B0)
    }
}