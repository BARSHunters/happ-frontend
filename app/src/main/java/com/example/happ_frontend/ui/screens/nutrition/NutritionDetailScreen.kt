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
import com.example.happ_frontend.model.nutrition.Meal
import com.example.happ_frontend.model.nutrition.MealType
import com.example.happ_frontend.model.nutrition.NutritionSummary

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
                    contentDescription = "Back",
                    tint = Color(0xFF9D89C5) // Purple color to match the design
                )
            }

            Text(
                text = "Today's Menu",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7B639C) // Purple color to match the design
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

            // Get meal by type
            val breakfast = meals.find { it.mealType == MealType.BREAKFAST }
            val lunch = meals.find { it.mealType == MealType.LUNCH }
            val dinner = meals.find { it.mealType == MealType.DINNER }
            val snack = meals.find { it.mealType == MealType.SNACK }

            if (breakfast != null) {
                MealCard(
                    title = "Breakfast",
                    meal = breakfast
                )
            }

            if (lunch != null) {
                MealCard(
                    title = "Lunch",
                    meal = lunch
                )
            }

            if (dinner != null) {
                MealCard(
                    title = "Dinner",
                    meal = dinner
                )
            }

            if (snack != null) {
                MealCard(
                    title = "Snack",
                    meal = snack,
                    isSnack = true
                )
            }
        }
    }
}

@Composable
fun NutritionSummary(summary: NutritionSummary) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF6F3FD) // Light purple background to match the design
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Daily Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7B639C) // Purple color to match the design
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
    meal: Meal,
    isSnack: Boolean = false
) {
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
                text = "$title: ${meal.name}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF7B639C) // Purple color to match the design
            )

            // Image placeholder (except for snack)
            if (!isSnack) {
                // In a real app, we would use Coil or Glide to load the actual image
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF3F0F9)) // Light purple background
                ) {
                    Text(
                        text = meal.name,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        color = Color(0xFF9D89C5), // Purple text color
                        style = MaterialTheme.typography.bodyMedium
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
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4A4A4A)
                    )

                    Text(
                        text = "Calories: ${meal.calories} kcal",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4A4A4A)
                    )

                    Text(
                        text = "Protein: ${meal.protein}g • Fat: ${meal.fat}g • Carbs: ${meal.carbs}g",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF4A4A4A)
                    )
                }

                if (!isSnack) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Portion Size",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4A4A4A)
                        )

                        Text(
                            text = meal.portionSize ?: "300g",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF4A4A4A)
                        )
                    }
                }
            }

            // Recipe link (except for snack)
            if (!isSnack && meal.recipeUrl != null) {
                TextButton(
                    onClick = { /* Open recipe */ },
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = Color(0xFF9D89C5) // Purple color for the button
                    )
                ) {
                    Text("View Recipe")
                }
            }
        }
    }
}