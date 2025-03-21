package com.example.happ_frontend.ui.screens.nutrition

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MealHistoryDetails() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("dd MMM - EEEE")

        Text(
            text = "${today.format(formatter)} - Today",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        MealItem(
            time = "10:00 am",
            mealName = "Cheeseburger",
            calories = 450
        )

        val yesterday = today.minusDays(1)
        Text(
            text = "${yesterday.format(formatter)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )

        MealItem(
            time = "08:00 am",
            mealName = "Shawarma",
            calories = 536
        )
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
            text = "$time - $mealName",
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = "(+$calories cal)",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}