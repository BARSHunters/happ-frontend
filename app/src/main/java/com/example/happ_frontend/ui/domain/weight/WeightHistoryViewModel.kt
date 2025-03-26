package com.example.happ_frontend.ui.domain.weight

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.happ_frontend.model.weight.WeightCalendarEvent
import com.example.happ_frontend.ui.domain.login_register.LoginRegisterValidationResult
import com.example.happ_frontend.ui.domain.login_register.LoginRegisterValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.reflect.KProperty

/**
 * A ViewModel for managing the weight history feature. It holds the weight events, predicted weight events,
 * and provides methods for adding, clearing, and validating weight events.
 */
class WeightHistoryViewModel : ViewModel() {
    private val _weightEvents = mutableStateListOf<WeightCalendarEvent>()
    val weightEvents get() = _weightEvents.toList()

    private val _predictedWeightEvents = mutableStateListOf<WeightCalendarEvent>()
    val predictedWeightEvents get() = _predictedWeightEvents.toList()

    private val data = MutableStateFlow(WeightHistoryFormData())

    val uiState: StateFlow<WeightHistoryFormData> = data.asStateFlow()

    var formShown by mutableStateOf(false)

    var visualizeShown by mutableStateOf(false)

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

    /**
     * Validates the input for adding a new weight event in the dialog.
     *
     * @return true if the input is valid, false otherwise.
     *
     * The validation checks if the entered weight is within the allowed range (between MIN_WEIGHT_KG and MAX_WEIGHT_KG).
     */
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

    /**
     * Resets the form data to its default state, with the option to keep specific fields unchanged.
     *
     * Updates the form data by resetting all fields to their default values,
     * except for those specified in the [keepFields] parameter. It also logs the reset process,
     * including which fields are kept, and the state before and after the reset.
     *
     * @param keepFields A set of KProperty objects representing the fields to keep unchanged.
     *                   Default is an empty set, which means all fields will be reset.
     */
    fun resetFormData(keepFields: Set<KProperty<*>> = emptySet()) {
        val currentData = uiState.value
        val newData = WeightHistoryFormData()
    
        data.update { form ->
            form.copy(
                entryDate = if (WeightHistoryFormData::entryDate in keepFields)
                    currentData.entryDate else newData.entryDate,
                entryTime = if (WeightHistoryFormData::entryTime in keepFields)
                    currentData.entryTime else newData.entryTime,
                entryWeightKgString = if (WeightHistoryFormData::entryWeightKgString in keepFields)
                    currentData.entryWeightKgString else newData.entryWeightKgString
            )
        }
    
        Log.d("WeightHistoryViewModel#resetFormData",
            "Resetting form data (keeping fields: ${
                keepFields.joinToString { it.name }
            })")
        Log.d("WeightHistoryViewModel#resetFormData",
            "Before reset: $currentData")
        Log.d("WeightHistoryViewModel#resetFormData",
            "After reset: ${uiState.value}")
    }
}