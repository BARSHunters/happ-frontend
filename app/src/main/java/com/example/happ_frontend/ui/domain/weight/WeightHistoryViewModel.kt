package com.example.happ_frontend.ui.domain.weight

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.happ_frontend.R
import com.example.happ_frontend.model.login_register.data.AuthSharedPreferencesEditor
import com.example.happ_frontend.model.login_register.data.AuthSharedPreferencesProvider
import com.example.happ_frontend.model.login_register.request.UserDataDto
import com.example.happ_frontend.model.weight.WeightCalendarEvent
import com.example.happ_frontend.model.weight.communication.WeightHistoryApiService
import com.example.happ_frontend.model.weight.communication.WeightHistoryNetworkModule
import com.example.happ_frontend.model.weight.data.WeightHistoryRepository
import com.example.happ_frontend.model.weight.kg
import com.example.happ_frontend.ui.domain.login_register.AuthValidationResult
import com.example.happ_frontend.ui.domain.login_register.AuthValidator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.reflect.KProperty

/**
 * A ViewModel for managing the weight history feature. It holds the weight events, predicted weight events,
 * and provides methods for adding, clearing, and validating weight events.
 */
class WeightHistoryViewModel(
    private val weightHistoryRepository: WeightHistoryRepository,
    private val weightHistoryApi: WeightHistoryApiService = WeightHistoryNetworkModule.weightHistoryApiService,
    private val authPrefs: AuthSharedPreferencesEditor = AuthSharedPreferencesProvider.editor
        ?: throw IllegalStateException("AuthSharedPreferencesEditor not initialized yet"),
): ViewModel() {
    private val _weightEvents = mutableStateListOf<WeightCalendarEvent>()
    val weightEvents get() = _weightEvents.toList()

    private val data = MutableStateFlow(WeightHistoryFormData())

    val uiState: StateFlow<WeightHistoryFormData> = data.asStateFlow()

    private var _weightHistoryState = MutableStateFlow<WeightHistoryState>(WeightHistoryState.Loading)
    val weightHistoryState: StateFlow<WeightHistoryState> = _weightHistoryState.asStateFlow()

    sealed interface WeightHistoryState {
        data object Loading: WeightHistoryState
        data object Success: WeightHistoryState
        data class Failure(
            @StringRes val messageRes: Int,
            val formatArgs: List<Any> = emptyList(),
            val isUnauthorizedError: Boolean = false
        ): WeightHistoryState
    }

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

    init {
        Log.d("WeightHistoryViewModel", "Initialized WeightHistoryViewModel")
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
            AuthValidator.NumberInputValidator (
                min = WeightHistoryFormData.MIN_WEIGHT_KG,
                max = WeightHistoryFormData.MAX_WEIGHT_KG,
                precision = 1
            ).validate(entryWeightKgString)
        ).all {
            it == AuthValidationResult.Success
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

    fun fetchWeightHistoryFromServer() {
        Log.d("WeightHistoryViewModel#addWeightHistoryEvent", "jwt: ${authPrefs.jwt}")
        if (authPrefs.jwt == null) {
            _weightHistoryState.value = WeightHistoryState.Failure(
                R.string.error_auth_noJwt,
                isUnauthorizedError = true
            )
            authPrefs.clearUserData()
            return
        }

        var isUnauthorizedError = false

        viewModelScope.launch {
            _weightHistoryState.value = WeightHistoryState.Loading
            try {
                val response = weightHistoryApi.getWeightHistory()
                _weightEvents.clear()
                if (response.isSuccessful) {
                    _weightHistoryState.value = WeightHistoryState.Success

                    val now = LocalDateTime.now()

                    response.body()?.let {
                        val weightHistoryMap = it.weightHistory
                        weightHistoryMap.forEach { (dateTime, value) ->
                            _weightEvents.add(WeightCalendarEvent(
                                dateTime = dateTime,
                                prediction = dateTime > now,
                                value = value.kg
                            ))
                        }
                    }
                } else {
                    if (response.code() == 401) {
                        isUnauthorizedError = true
                    }

                    _weightHistoryState.value = WeightHistoryState.Failure(
                        R.string.error_weight_weightHistory_loadFail,
                        isUnauthorizedError = isUnauthorizedError
                    )
                    _weightEvents.clear()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _weightHistoryState.value = e.message?.let { msg ->
                    WeightHistoryState.Failure(R.string.error_misc_errorPrefix, formatArgs = listOf(msg))
                } ?: WeightHistoryState.Failure(R.string.error_misc_unknown)
                _weightEvents.clear()
            } finally {
                if (isUnauthorizedError) {
                    authPrefs.clearUserData()
                }
            }
        }
    }

    fun addWeightHistoryEvent(weight: Float) {
        Log.d("WeightHistoryViewModel#addWeightHistoryEvent", "jwt: ${authPrefs.jwt}")
        if (authPrefs.jwt == null) {
            _weightHistoryState.value = WeightHistoryState.Failure(
                R.string.error_auth_noJwt,
                isUnauthorizedError = true
            )
            authPrefs.clearUserData()
            return
        }

        @StringRes
        var errorMessageRes: Int? = null
        var isUnauthorizedError: Boolean = false

        viewModelScope.launch {
            try {
                _weightHistoryState.value = WeightHistoryState.Loading
                val userInfoResponse = weightHistoryApi.getUserInfo()
                if (!userInfoResponse.isSuccessful) {
                    errorMessageRes = R.string.error_weight_userData_loadFail
                    if (userInfoResponse.code() == 401) {
                        isUnauthorizedError = true
                    }
                    throw IllegalArgumentException()
                }
                userInfoResponse.body()?.let { userDataResponseBody ->
                    val userDataRequest = UserDataDto
                        .fromResponseDto(userDataResponseBody)
                        .copy(weight = weight)
                    val updateUserInfoResponse = weightHistoryApi.updateInfo(userDataRequest)
                    if (!updateUserInfoResponse.isSuccessful) {
                        errorMessageRes = R.string.error_weight_userData_updateFail
                        throw IllegalArgumentException()
                    }
                    fetchWeightHistoryFromServer()
                }
                if (userInfoResponse.body() == null) {
                    errorMessageRes = R.string.error_weight_userData_loadFail
                    throw IllegalArgumentException()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _weightHistoryState.value = WeightHistoryState.Failure(
                    errorMessageRes ?: R.string.error_misc_unknown,
                    isUnauthorizedError = isUnauthorizedError
                )
                _weightEvents.clear()
            } finally {
                if (isUnauthorizedError) {
                    authPrefs.clearUserData()
                }
            }
        }
    }
}