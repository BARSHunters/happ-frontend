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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun NutritionDetailScreen(
    onBackClick: () -> Unit
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
            NutritionSummary()

            MealCard(
                title = "Breakfast",
                calories = 450,
                protein = 20,
                fat = 25,
                carbs = 35
            )

            MealCard(
                title = "Lunch",
                calories = 650,
                protein = 35,
                fat = 20,
                carbs = 75
            )

            MealCard(
                title = "Dinner",
                calories = 550,
                protein = 30,
                fat = 18,
                carbs = 60
            )

            MealCard(
                title = "Snack",
                calories = 200,
                protein = 5,
                fat = 8,
                carbs = 25,
                isSnack = true
            )
        }
    }
}

@Composable
fun NutritionSummary() {
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
                NutrientItem("Calories", "1850", Color(0xFF8BC34A))
                NutrientItem("Protein", "90g", Color(0xFF03A9F4))
                NutrientItem("Fat", "71g", Color(0xFFFF9800))
                NutrientItem("Carbs", "195g", Color(0xFFE91E63))
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
    isSnack: Boolean = false
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
                text = title,
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
                        text = "Meal Image",
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
                            text = "300g",
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

