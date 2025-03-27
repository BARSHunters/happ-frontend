package com.example.happ_frontend.ui.domain.notifications

import androidx.lifecycle.ViewModel
import com.example.happ_frontend.model.notifications.NotificationData
import com.example.happ_frontend.model.repository.NotificationModelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotificationViewModel : ViewModel() {
    private val notificationRepository = NotificationModelRepository()
    private val notifications = notificationRepository.getAll()
    private val _uiState = MutableStateFlow(notifications)
    val uiState : StateFlow<List<NotificationData>> = _uiState.asStateFlow()
    fun updateData(){
        _uiState.update { notificationData ->
            notificationData.drop(1).toMutableList()
        }
    }
}