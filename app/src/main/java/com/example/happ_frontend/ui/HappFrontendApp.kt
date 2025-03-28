package com.example.happ_frontend.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.happ_frontend.ui.navigation.HappFrontendNavigationHost

@Composable
fun HappFrontendApp(){
    val navigationController = rememberNavController()
    HappFrontendNavigationHost(navigationController = navigationController)
}