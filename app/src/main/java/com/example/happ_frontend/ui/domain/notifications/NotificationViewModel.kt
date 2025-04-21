package com.example.happ_frontend.ui.domain.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.happ_frontend.model.notifications.NotificationData
import com.example.happ_frontend.model.notifications.data.NotificationConverter
import com.example.happ_frontend.model.notifications.data.NotificationsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NotificationViewModel(private val notificationsRepository: NotificationsRepository) : ViewModel() {
    private var notifications = emptyList<NotificationData>()//notificationsRepository.getAllNotificationsStream().map { NotificationConverter.fromEntity(it) }
    init {
        updateData()
    }
    private val _uiState = MutableStateFlow(notifications)
    val uiState : StateFlow<List<NotificationData>> = _uiState.asStateFlow()
    fun updateData(){
        viewModelScope.launch {
            val new = notificationsRepository.getAllNotificationsStream().map { NotificationConverter.fromEntity(it) }
            _uiState.update {
                new
            }
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            notificationsRepository.deleteAll()
            updateData()
        }
    }
}