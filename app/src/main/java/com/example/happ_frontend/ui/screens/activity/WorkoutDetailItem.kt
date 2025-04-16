package com.example.happ_frontend.ui.screens.activity

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.happ_frontend.model.activity.Workout

@Composable
fun IntensityIndicator(intensity: Int, modifier: Modifier = Modifier) {
    val colors = when {
        intensity < 40 -> listOf(Color(0xFF8BC34A), Color(0xFF4CAF50))
        intensity < 60 -> listOf(Color(0xFF4CAF50), Color(0xFFFFEB3B))
        intensity < 80 -> listOf(Color(0xFFFF9800), Color(0xFFFF5722))
        else -> listOf(Color(0xFFFF5722), Color(0xFFE91E63))
    }

    val intensityText = when {
        intensity < 40 -> "Низкая"
        intensity < 60 -> "Средняя"
        intensity < 80 -> "Высокая"
        else -> "Максимальная"
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Интенсивность",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .height(8.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(4.dp))
                .background(Color(0xFFE0E0E0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(intensity / 100f)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        brush = Brush.horizontalGradient(colors)
                    )
            )
        }
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = intensityText,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
            
            Text(
                text = "$intensity%",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

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
                        text = "${workout.time}   ${workout.duration} мин",
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
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Индикатор интенсивности
            IntensityIndicator(
                intensity = workout.intensity,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
    }
} 