package com.example.happ_frontend.ui.domain.notifications

import androidx.lifecycle.ViewModel
import com.example.happ_frontend.model.notifications.NotificationData
import com.example.happ_frontend.model.repository.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationViewModel : ViewModel() {
    private val notificationRepository = NotificationRepository()
    private val _uiState = MutableStateFlow(notificationRepository.getAll())
    val uiState : StateFlow<List<NotificationData>> = _uiState.asStateFlow()
}