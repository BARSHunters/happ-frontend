package com.example.happ_frontend.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.rememberNavController
import com.example.happ_frontend.ui.navigation.ActivityDest
import com.example.happ_frontend.ui.navigation.HappFrontendNavigationHost
import com.example.happ_frontend.ui.navigation.NutritionDest
import com.example.happ_frontend.ui.screens.nutrition.NutritionScreen

@Composable
fun HappFrontendApp(){
    val navigationController = rememberNavController()
    HappFrontendNavigationHost(navigationController = navigationController)



}