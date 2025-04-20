package com.example.happ_frontend.ui.screens.nutrition

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.happ_frontend.model.nutrition.MealDay
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MealHistoryDetails(
    mealDay: MealDay?,
    today: LocalDate = LocalDate.now()
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF6F3FD)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (mealDay != null) {
                val formatter = DateTimeFormatter.ofPattern("dd MMM - EEEE")
                val isToday = mealDay.date.equals(today)

                Text(
                    text = "${mealDay.date.format(formatter)}${if (isToday) " - Today" else ""}",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                mealDay.meals.forEach { meal ->
                    MealItem(
                        time = meal.time,
                        mealName = meal.name,
                        calories = meal.calories
                    )
                }
            } else {
                Text(
                    text = "No meal data available for this date",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun MealItem(
    time: String,
    mealName: String,
    calories: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$time - ${mealName.trim()}",
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        )

        Text(
            text = "(+$calories cal)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}