package com.example.happ_frontend.ui.domain.user_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.happ_frontend.model.user_info.communication.UserInfoNetworkModule
import com.example.happ_frontend.ui.navigation.NavigateToLoginUIEvent
import com.example.happ_frontend.ui.navigation.UIEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserInfoOtherPersonViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UserInfoUIState())
    val uiState: StateFlow<UserInfoUIState> = _uiState.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UIEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun loadUserInfo(username: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(status = UserInfoStatus.LOADING) }
            try {
                val response = UserInfoNetworkModule.userInfoApiService.getUserInfo(username)
                if (response.code() == 401) {
                    _eventFlow.emit(NavigateToLoginUIEvent)
                    return@launch
                }

                if (response.isSuccessful && response.body() != null) {
                    val userInfo = response.body()
                    println(userInfo)
                    _uiState.update {
                        it.copy(
                            status = UserInfoStatus.SHOW_DATA,
                            name = userInfo!!.name,
                            username = response.body()!!.username,
                            birthDate = response.body()!!.birthDate.toString(),
                            gender = response.body()!!.gender,
                            currentWeight = response.body()!!.weight,
                            goalWeight = response.body()!!.weightDesire,
                            height = response.body()!!.height.toFloat()
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
}

