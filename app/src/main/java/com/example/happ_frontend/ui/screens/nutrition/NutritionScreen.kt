package com.example.happ_frontend.ui.screens.nutrition

import android.util.Log
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
    var screenState by remember { mutableStateOf<NutritionScreenState>(NutritionScreenState.Main) }
    val TAG = "NutritionScreen"

    // Показываем ошибку, если она есть
    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            Log.e(TAG, "Ошибка: ${uiState.error}")
        }
    }

    // Only show the current screen based on state
    when (val currentState = screenState) {
        is NutritionScreenState.Main -> {
            MainNutritionScreen(
                viewModel = viewModel,
                onBackClick = onBackClick,
                onDetailClick = { screenState = NutritionScreenState.Detail },
                onCreateMenuClick = {
                    viewModel.createNewMenuForToday()
                    screenState = NutritionScreenState.Detail
                }
            )
        }
        is NutritionScreenState.Detail -> {
            NutritionDetailScreen(
                onBackClick = { screenState = NutritionScreenState.Main },
                meals = uiState.currentMealDay?.meals ?: emptyList(),
                nutritionSummary = viewModel.getTotalNutrition(uiState.currentMealDay?.meals ?: emptyList())
            )
        }
    }
}

@Composable
fun MainNutritionScreen(
    viewModel: NutritionViewModel,
    onBackClick: () -> Unit,
    onDetailClick: () -> Unit,
    onCreateMenuClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val TAG = "NutritionScreen"

    LaunchedEffect(Unit) {
        viewModel.loadInitialData()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
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
                NutritionHeader(
                    onDetailClick = {
                        if (uiState.currentMealDay?.meals?.isNotEmpty() == true) {
                            onDetailClick()
                        }
                    },
                    meals = uiState.currentMealDay?.meals?.map { it.name } ?: emptyList()
                )

                // Calendar to select dates
                MealHistoryCalendar(
                    selectedDate = uiState.selectedDate,
                    onDateSelected = { date ->
                        Log.d(TAG, "Выбрана дата: $date")
                        viewModel.loadNutritionForDate(date)
                    }
                )

                // Meal details for the selected date
                MealHistoryDetails(mealDay = uiState.currentMealDay)

                Spacer(modifier = Modifier.weight(1f))

                // Button to create a new menu
                Button(
                    onClick = {
                        viewModel.createNewMenuForToday()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    enabled = !uiState.isLoading
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Создать новое меню")
                    }
                }

                // Показываем ошибку, если она есть
                uiState.error?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
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

sealed class NutritionScreenState {
    object Main : NutritionScreenState()
    object Detail : NutritionScreenState()
}