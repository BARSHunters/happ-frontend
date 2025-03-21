package com.example.happ_frontend.ui.screens.nutrition

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.happ_frontend.ui.viewmodels.NutritionViewModel

@Composable
fun NutritionScreen(
    viewModel: NutritionViewModel = viewModel()
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = "Nutrition",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
            }

            NutritionHeader(onDetailClick = {
                if (uiState.currentMealDay?.meals?.isNotEmpty() == true) {
                    showDetailScreen = true
                }
            })

            MealHistoryCalendar(onDateSelected = { date ->
                viewModel.loadNutritionMenuForDate(date)
            })

            MealHistoryDetails(mealDay = uiState.currentMealDay)

            Spacer(modifier = Modifier.weight(1f))

            CreateMenuButton(onClick = { viewModel.createNewMenu() })

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}