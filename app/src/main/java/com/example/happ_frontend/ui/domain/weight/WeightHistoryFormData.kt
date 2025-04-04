package com.example.happ_frontend.ui.domain.weight

import com.example.happ_frontend.ui.domain.login_register.now
import java.time.LocalDate
import java.time.LocalTime

/**
 * Data class representing the form data for weight history entries.
 * @property entryDate The date of the weight entry. Defaults to the current date.
 * @property entryTime The time of the weight entry. Defaults to the current time.
 * @property entryWeightKgString The weight entry in kilograms as a string. Defaults to [DEFAULT_WEIGHT_KG].
 * @author Vad1mChK
 */
data class WeightHistoryFormData(
    val entryDate: LocalDate = LocalDate.now(),
    val entryTime: LocalTime = LocalTime.now(),
    val entryWeightKgString: String = DEFAULT_WEIGHT_KG.toString()
) {
    companion object {
        const val DEFAULT_WEIGHT_KG = 70.0f
        const val MIN_WEIGHT_KG = 40.0f
        const val MAX_WEIGHT_KG = 200.0f
    }
}