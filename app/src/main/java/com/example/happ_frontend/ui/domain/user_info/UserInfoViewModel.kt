package com.example.happ_frontend.ui.domain.user_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.happ_frontend.model.user_info.UserInfoData
import com.example.happ_frontend.model.user_info.data.UserInfoConverter
import com.example.happ_frontend.model.user_info.data.UserInfoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserInfoViewModel(
    private val userInfoRepository: UserInfoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UserInfoData())
    val uiState: StateFlow<UserInfoData> = _uiState.asStateFlow()

    init {
        fetchUserData()
    }

    fun fetchUserData() {
        viewModelScope.launch {
            val entity = userInfoRepository.getUserInfo()
            val data = UserInfoConverter.fromEntity(entity)
            _uiState.value = data
        }
    }

    fun updateName(newName: String) {
        _uiState.update { it.copy(name = newName) }
    }

    fun updateWeight(weight: Float) {
        _uiState.update { it.copy(weight = weight) }
    }

    fun updateGoalWeight(goal: Float) {
        _uiState.update { it.copy(goalWeight = goal) }
    }

    fun updateHeight(height: Float) {
        _uiState.update { it.copy(height = height) }
    }

    fun saveUserData() {
        viewModelScope.launch {
            val entity = UserDataConverter.toEntity(_uiState.value)
            userInfoRepository.saveUserInfo(entity)
        }
    }

    fun logout() {
        userInfoRepository.clearJwt()
    }
}
