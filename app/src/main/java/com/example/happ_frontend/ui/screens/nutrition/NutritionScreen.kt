package com.example.happ_frontend.ui.screens.nutrition

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.happ_frontend.model.nutrition.NutritionViewModel

@Composable
fun NutritionScreen(
    viewModel: NutritionViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDetailScreen by remember { mutableStateOf(false) }

    if (showDetailScreen) {
        NutritionDetailScreen(
            onBackClick = { showDetailScreen = false },
            meals = uiState.currentMealDay?.meals ?: emptyList(),
            nutritionSummary = viewModel.getTotalNutrition(uiState.currentMealDay?.meals ?: emptyList())
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with back button and centered title
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
                        tint = Color(0xFFA590B6) // Purple color
                    )
                }

                Text(
                    text = "Nutrition",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF7B639C), // Purple color
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            // Banner with nutrition information
            NutritionHeader(onDetailClick = {
                if (uiState.currentMealDay?.meals?.isNotEmpty() == true) {
                    showDetailScreen = true
                }
            })

            // Calendar to select dates
            MealHistoryCalendar(
                selectedDate = uiState.selectedDate,
                onDateSelected = { date ->
                    viewModel.loadNutritionMenuForDate(date)
                }
            )

            // Meal details for the selected date
            MealHistoryDetails(mealDay = uiState.currentMealDay)

            Spacer(modifier = Modifier.weight(1f))

            // Button to create a new menu
            CreateMenuButton(onClick = {
                viewModel.createNewMenuForToday()
                showDetailScreen = true // Show detail screen after creating a new menu
            })

            // Loading indicator
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = Color(0xFFA590B6) // Purple color to match the design
                    )
                }
            }
        }
    }
}