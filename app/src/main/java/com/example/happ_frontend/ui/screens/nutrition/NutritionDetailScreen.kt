package com.example.happ_frontend.ui.screens.nutrition

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
import androidx.compose.ui.unit.dp
import com.example.happ_frontend.ui.viewmodels.Meal
import com.example.happ_frontend.ui.viewmodels.NutritionSummary

@Composable
fun NutritionDetailScreen(
    onBackClick: () -> Unit,
    meals: List<Meal>,
    nutritionSummary: NutritionSummary
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top app bar with back button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }

            Text(
                text = "Today's Menu",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        // Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            NutritionSummary(nutritionSummary)

            // Get breakfast, lunch, and dinner
            val breakfast = meals.findMealByTimeRange("06:00", "10:00")
            val lunch = meals.findMealByTimeRange("11:00", "14:00")
            val dinner = meals.findMealByTimeRange("17:00", "21:00")
            val snack = meals.findMealByTimeRange("14:00", "17:00")

            if (breakfast != null) {
                MealCard(
                    title = "Breakfast",
                    calories = breakfast.calories,
                    protein = breakfast.protein ?: 0,
                    fat = breakfast.fat ?: 0,
                    carbs = breakfast.carbs ?: 0,
                    portionSize = breakfast.portionSize ?: "300g",
                    name = breakfast.name
                )
            }

            if (lunch != null) {
                MealCard(
                    title = "Lunch",
                    calories = lunch.calories,
                    protein = lunch.protein ?: 0,
                    fat = lunch.fat ?: 0,
                    carbs = lunch.carbs ?: 0,
                    portionSize = lunch.portionSize ?: "300g",
                    name = lunch.name
                )
            }

            if (dinner != null) {
                MealCard(
                    title = "Dinner",
                    calories = dinner.calories,
                    protein = dinner.protein ?: 0,
                    fat = dinner.fat ?: 0,
                    carbs = dinner.carbs ?: 0,
                    portionSize = dinner.portionSize ?: "300g",
                    name = dinner.name
                )
            }

            if (snack != null) {
                MealCard(
                    title = "Snack",
                    calories = snack.calories,
                    protein = snack.protein ?: 0,
                    fat = snack.fat ?: 0,
                    carbs = snack.carbs ?: 0,
                    isSnack = true,
                    name = snack.name
                )
            }
        }
    }
}

@Composable
fun NutritionSummary(summary: NutritionSummary) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Daily Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                NutrientItem("Calories", "${summary.calories}", Color(0xFF8BC34A))
                NutrientItem("Protein", "${summary.protein}g", Color(0xFF03A9F4))
                NutrientItem("Fat", "${summary.fat}g", Color(0xFFFF9800))
                NutrientItem("Carbs", "${summary.carbs}g", Color(0xFFE91E63))
            }
        }
    }
}

@Composable
fun NutrientItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
fun MealCard(
    title: String,
    calories: Int,
    protein: Int,
    fat: Int,
    carbs: Int,
    portionSize: String = "300g",
    isSnack: Boolean = false,
    name: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "$title: $name",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            // Placeholder for image (except for snack)
            if (!isSnack) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Text(
                        text = name,
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Nutrition info
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Nutrition",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Calories: $calories kcal",
                        style = MaterialTheme.typography.bodySmall
                    )

                    Text(
                        text = "Protein: ${protein}g • Fat: ${fat}g • Carbs: ${carbs}g",
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                if (!isSnack) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Portion Size",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = portionSize,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Recipe link (except for snack)
            if (!isSnack) {
                TextButton(
                    onClick = { /* Open recipe */ },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("View Recipe")
                }
            }
        }
    }
}

// Extension function to find a meal within a specific time range
fun List<Meal>.findMealByTimeRange(startHour: String, endHour: String): Meal? {
    // Simple implementation - just find a meal that might be in that time range based on its time property
    // In a real app, we'd parse the time and do proper time comparisons
    return this.find { meal ->
        val hourOnly = meal.time.split(":").firstOrNull()?.trim() ?: ""
        when {
            hourOnly.contains("am") && startHour.contains("0") -> true
            hourOnly.contains("pm") && startHour.contains("1") -> true
            else -> false
        }
    }
}