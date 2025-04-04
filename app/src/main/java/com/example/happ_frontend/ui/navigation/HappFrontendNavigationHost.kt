package com.example.happ_frontend.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.happ_frontend.ui.screens.home.HomeScreen
import com.example.happ_frontend.ui.screens.login_register.LoginScreen
import com.example.happ_frontend.ui.screens.login_register.RegisterScreen
import com.example.happ_frontend.ui.screens.notifications.NotificationScreen
import com.example.happ_frontend.ui.screens.weight.WeightHistoryScreen

@Composable
fun HappFrontendNavigationHost(
    navigationController: NavHostController,
    modifier: Modifier = Modifier
) {
    Scaffold { innerPadding ->
        NavHost(
            modifier = modifier.padding(innerPadding),
            navController = navigationController,
            startDestination = HomeDest.route // NotificationDest.route
        ) {
            composable(route = NotificationDest.route) {
                NotificationScreen()
            }
            composable(route = LoginDest.route) {
                LoginScreen(
                    navigationController,
                    viewModel = viewModel()
                )
            }
            composable(route = RegisterDest.route) {
                RegisterScreen(
                    navigationController,
                    viewModel = viewModel()
                )
            }
            composable(route = HomeDest.route) {
                HomeScreen(
                    navigationController
                )
            }
            composable(route = WeightHistoryDest.route) {
                WeightHistoryScreen(
                    navigationController,
                    viewModel = viewModel()
                )
            }
         }
    }
}