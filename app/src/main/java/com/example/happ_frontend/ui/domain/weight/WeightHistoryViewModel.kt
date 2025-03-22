package com.example.happ_frontend.ui.domain.weight

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.happ_frontend.ui.domain.login_register.LoginRegisterValidationResult
import com.example.happ_frontend.ui.domain.login_register.LoginRegisterValidator
import com.example.happ_frontend.ui.domain.login_register.LoginRegisterViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

class WeightHistoryViewModel : ViewModel() {
    private val _weightEvents = mutableStateListOf<WeightCalendarEvent>()
    val weightEvents get() = _weightEvents.toList()

    private val _predictedWeightEvents = mutableStateListOf<WeightCalendarEvent>()
    val predictedWeightEvents get() = _predictedWeightEvents.toList()

    private val data = MutableStateFlow(WeightHistoryFormData())
    val uiState: StateFlow<WeightHistoryFormData> = data.asStateFlow()

    var formShown: Boolean
        get() = uiState.value.formShown
        set(value) {
            data.update { form -> form.copy(formShown = value) }
        }

    var entryDate: LocalDate
        get() = uiState.value.entryDate
        set(value) {
            data.update { form -> form.copy(entryDate = value) }
        }

    var entryTime: LocalTime
        get() = uiState.value.entryTime
        set(value) {
            data.update { form -> form.copy(entryTime = value) }
        }

    var entryWeightKgString: String
        get() = uiState.value.entryWeightKgString
        set(value) {
            data.update { form -> form.copy(entryWeightKgString = value) }
        }

    fun addWeightEvent(weightCalendarEvent: WeightCalendarEvent) {
        _weightEvents.add(weightCalendarEvent)
    }

    fun clearWeightEvents() {
        _weightEvents.clear()
    }

    fun addPredictedWeightEvent(weightCalendarEvent: WeightCalendarEvent) {
        _predictedWeightEvents.add(weightCalendarEvent)
    }

    fun clearPredictedWeightEvents() {
        _predictedWeightEvents.clear()
    }

    fun setAllPredictedWeightEvents(newEvents: List<WeightCalendarEvent>) {
        _predictedWeightEvents.clear()
        _predictedWeightEvents.addAll(newEvents)
    }

    fun validateAddEventDialog(): Boolean {
        return listOf(
            LoginRegisterValidator.NumberInputValidator (
                min = WeightHistoryFormData.MIN_WEIGHT_KG,
                max = WeightHistoryFormData.MAX_WEIGHT_KG,
                precision = 1
            ).validate(entryWeightKgString)
        ).all {
            it == LoginRegisterValidationResult.Success
        }
    }
}