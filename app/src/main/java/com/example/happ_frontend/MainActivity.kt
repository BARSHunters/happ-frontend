package com.example.happ_frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.happ_frontend.ui.HappFrontendApp
import com.example.happ_frontend.ui.navigation.NutritionDest
import com.example.happ_frontend.ui.theme.HappfrontendTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappfrontendTheme {
                HappFrontendApp()
            }
        }
    }
}
