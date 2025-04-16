package com.example.happ_frontend.ui.screens.nutrition

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.happ_frontend.R
import com.example.happ_frontend.model.nutrition.NutritionViewModel
import com.example.happ_frontend.ui.screens.weight.PageHeaderWithBackButton
import java.time.LocalDate

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
                .padding(paddingValues = PaddingValues(
                    start = 32.dp,
                    end = 32.dp,
                    top = 32.dp
                )),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
            horizontalAlignment = Alignment.Start
        ) {
            // Header with back button and title
            PageHeaderWithBackButton(
                title = stringResource(R.string.health_category_nutrition_page_title),
                onGoBack = onBackClick
            )

            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(0.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
                horizontalAlignment = Alignment.Start
            ) {
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
            }

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