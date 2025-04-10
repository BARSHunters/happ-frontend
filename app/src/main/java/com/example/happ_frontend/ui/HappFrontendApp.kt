package com.example.happ_frontend.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.example.happ_frontend.ui.navigation.ActivityDest
import com.example.happ_frontend.ui.navigation.HappFrontendNavigationHost

@Composable
fun HappFrontendApp(){
    val navigationController = rememberNavController()

    //TODO fix
    LaunchedEffect(navigationController) {
        navigationController.navigate(ActivityDest.route) {
            popUpTo(navigationController.graph.startDestinationId) {
                inclusive = true
            }
        }
    }

    HappFrontendNavigationHost(navigationController = navigationController)
}