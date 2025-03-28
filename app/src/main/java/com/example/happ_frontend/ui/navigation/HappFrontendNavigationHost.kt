package com.example.happ_frontend.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.happ_frontend.ui.screens.notifications.NotificationScreen

@Composable
fun HappFrontendNavigationHost(
    navigationController: NavHostController,
    modifier: Modifier = Modifier
) {
    Scaffold { innerPadding ->
        NavHost(
            modifier = modifier.padding(innerPadding),
            navController = navigationController,
            startDestination = NotificationDest.route
        ) {
            composable(route = NotificationDest.route) {
                NotificationScreen()
            }
        }
    }
}