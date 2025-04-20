package com.example.happ_frontend.ui.domain.user_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.happ_frontend.model.user_info.communication.UserInfoNetworkModule
import com.example.happ_frontend.model.user_info.request.Gender
import com.example.happ_frontend.model.user_info.request.UserDataDTO
import com.example.happ_frontend.model.user_info.request.WeightDesire
import com.example.happ_frontend.ui.navigation.NavigateToLoginUIEvent
import com.example.happ_frontend.ui.navigation.UIEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate


class UserInfoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UserInfoUIState())
    val uiState: StateFlow<UserInfoUIState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()


    init {
        loadUserInfo()
        loadFriends()
    }

    fun loadUserInfo() {
        viewModelScope.launch {
            _uiState.update { it.copy(status = UserInfoStatus.LOADING) }
            try {
                val response = UserInfoNetworkModule.userInfoApiService.getUserInfo()
                if (response.code() == 401) {
                    _eventFlow.emit(NavigateToLoginUIEvent)
                    return@launch
                }

                if (response.isSuccessful && response.body() != null) {
                    val userInfo = response.body()
                    _uiState.update {
                        it.copy(
                            status = UserInfoStatus.SHOW_DATA,
                            name = userInfo!!.name,
                            username = response.body()!!.username,
                            birthDate = response.body()!!.birthDate.toString(),
                            gender = response.body()!!.gender,
                            currentWeight = response.body()!!.weightKg,
                            goalWeight = response.body()!!.weightDesire,
                            height = response.body()!!.heightCm.toFloat()
                        )
                    }
                } else {
                    _uiState.update { it.copy(status = UserInfoStatus.ERROR) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(status = UserInfoStatus.ERROR) }
            }
        }
    }

    fun loadFriends() {
        viewModelScope.launch {
            try {
                val response = UserInfoNetworkModule.userInfoApiService.getFriends()
                if (response.code() == 401) {
                    _eventFlow.emit(NavigateToLoginUIEvent)
                    return@launch
                }

                if (response.isSuccessful && response.body() != null) {
                    _uiState.update {
                        it.copy(
                            friends = response.body()!!.friends,
                            friendsCount = response.body()!!.friendsCount
                        )
                    }
                }
            } catch (_: Exception) {
            }
        }
    }

    fun updateName(name: String) {
        _uiState.update { it.copy(name = name) }
        save()
    }

    fun updateCurrentWeight(weight: Float) {
        _uiState.update { it.copy(currentWeight = weight) }
    }


    fun updateGoalWeightDesire(desire: WeightDesire) {
        _uiState.update { it.copy(goalWeight = desire) }
        save()
    }

    fun updateHeight(height: Float) {
        _uiState.update { it.copy(height = height) }
    }

    fun updateGender(gender: Gender) {
        _uiState.update { it.copy(gender = gender) }
        save()
    }

    fun save() {
        viewModelScope.launch {
            val state = _uiState.value

            val userData = UserDataDTO(
                username = state.username,
                name = state.name,
                birthDate = LocalDate.parse(state.birthDate),
                gender = state.gender,
                heightCm = state.height.toInt(),
                weightKg = state.currentWeight,
                weightDesire = state.goalWeight
            )

            try {
                val response = UserInfoNetworkModule.userInfoApiService.updateInfo(userData)
                if (!response.isSuccessful) {
                    _uiState.update { it.copy(status = UserInfoStatus.ERROR) }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(status = UserInfoStatus.ERROR) }
            }
        }
    }

}


enum class UserInfoStatus {
    LOADING,
    SHOW_DATA,
    ERROR
}

data class UserInfoUIState(
    val username: String = "",
    val name: String = "",
    val birthDate: String = "",
    val gender: Gender = Gender.MALE,
    val currentWeight: Float = 0f,
    val goalWeight: WeightDesire = WeightDesire.GAIN,
    val height: Float = 0f,
    val friends: List<String> = emptyList(),
    val friendsCount: Int = 0,
    val status: UserInfoStatus = UserInfoStatus.LOADING
)
