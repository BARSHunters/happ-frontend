package com.example.happ_frontend.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.happ_frontend.HappFrontendApplication
import com.example.happ_frontend.ui.domain.notifications.NotificationViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY


object AppViewModelProvider{
    val Factory : ViewModelProvider.Factory = viewModelFactory {
        initializer {
            NotificationViewModel(happFrontendApplication().container.notificationsRepository)
        }
    }
}

fun CreationExtras.happFrontendApplication(): HappFrontendApplication =
    (this[APPLICATION_KEY] as HappFrontendApplication)