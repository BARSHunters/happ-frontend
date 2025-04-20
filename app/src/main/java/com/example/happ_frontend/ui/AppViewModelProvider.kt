package com.example.happ_frontend.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.happ_frontend.HappFrontendApplication
import com.example.happ_frontend.ui.domain.notifications.NotificationViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.happ_frontend.model.login_register.communication.AuthNetworkModule
import com.example.happ_frontend.model.login_register.data.AuthSharedPreferencesEditor
import com.example.happ_frontend.model.weight.communication.WeightHistoryNetworkModule
import com.example.happ_frontend.ui.domain.login_register.AuthViewModel
import com.example.happ_frontend.ui.domain.user_info.UserInfoViewModel
import com.example.happ_frontend.ui.domain.weight.WeightHistoryViewModel


object AppViewModelProvider{
    val Factory : ViewModelProvider.Factory = viewModelFactory {
        initializer {
            NotificationViewModel(happFrontendApplication().container.notificationsRepository)
        }
        initializer {
            WeightHistoryViewModel(
                weightHistoryRepository = happFrontendApplication().container.weightHistoryRepository,
                weightHistoryApi = WeightHistoryNetworkModule.weightHistoryApiService
            )
        }
        initializer {
            AuthViewModel(
                authApi = AuthNetworkModule.authApiService, // Direct access
                prefs = AuthSharedPreferencesEditor(
                    context = happFrontendApplication().applicationContext
                )
            )
        }
        initializer {
            UserInfoViewModel(
                userInfoRepository = happFrontendApplication().container.userInfoRepository,

            )
        }
    }
}

fun CreationExtras.happFrontendApplication(): HappFrontendApplication =
    (this[APPLICATION_KEY] as HappFrontendApplication)