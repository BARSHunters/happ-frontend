package com.example.happ_frontend.ui.domain.weight

import com.example.happ_frontend.ui.domain.login_register.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class WeightHistoryFormData(
    val formShown: Boolean = false,
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