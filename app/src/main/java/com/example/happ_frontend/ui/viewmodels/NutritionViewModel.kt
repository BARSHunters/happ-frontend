package com.example.happ_frontend.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class NutritionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NutritionUiState())
    val uiState: StateFlow<NutritionUiState> = _uiState.asStateFlow()

    init {
        loadNutritionMenuForDate(LocalDate.now())
    }

    fun loadNutritionMenuForDate(date: LocalDate) {
        viewModelScope.launch {
            // TODO: Call API to get nutrition menu for the selected date
            // Example: api.getNutritionMenu(date)

            // For now, just update with dummy data
            _uiState.value = _uiState.value.copy(
                selectedDate = date,
                isLoading = false
            )
        }
    }

    fun createNewMenu() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // TODO: Call API to create a new menu
            // Example: api.getNutritionMenu()

            // After API call, update UI state
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }
}

data class NutritionUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val menuHistory: List<MealDay> = emptyList(),
    val isLoading: Boolean = false
)

data class MealDay(
    val date: LocalDate,
    val meals: List<Meal>
)

data class Meal(
    val time: String,
    val name: String,
    val calories: Int,
    val protein: Int? = null,
    val fat: Int? = null,
    val carbs: Int? = null,
    val imageUrl: String? = null,
    val recipeUrl: String? = null
)