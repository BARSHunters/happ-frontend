package com.example.happ_frontend.ui.screens.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.happ_frontend.model.activity.Workout

@Composable
fun WorkoutDetailItem(workout: Workout) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF6F3FD)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = workout.name,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = workout.time,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                
                Text(
                    text = "${workout.calories} ккал",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF9D89C5)
                )
            }
            
            // Зоны интенсивности
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Text(
                    text = "Зоны интенсивности:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IntensityZoneItem(
                        label = "Очень легкая",
                        minutes = workout.intensityZones[0],
                        color = Color(0xFFA8D5BA)
                    )
                    IntensityZoneItem(
                        label = "Легкая",
                        minutes = workout.intensityZones[1],
                        color = Color(0xFF7BC8A4)
                    )
                    IntensityZoneItem(
                        label = "Умеренная",
                        minutes = workout.intensityZones[2],
                        color = Color(0xFF4DBB8E)
                    )
                    IntensityZoneItem(
                        label = "Высокая",
                        minutes = workout.intensityZones[3],
                        color = Color(0xFF1FAD78)
                    )
                    IntensityZoneItem(
                        label = "Максимальная",
                        minutes = workout.intensityZones[4],
                        color = Color(0xFF009F62)
                    )
                }
            }
        }
    }
}

@Composable
fun IntensityZoneItem(label: String, minutes: Int, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(color)
        )
        Text(
            text = "$minutes",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
} 