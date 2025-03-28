package com.example.happ_frontend.ui.domain

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.happ_frontend.ui.screens.nutrition.NutritionScreen
import com.example.happ_frontend.ui.theme.HappfrontendTheme

class NutritionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HappfrontendTheme {
                Surface(
                    modifier = Modifier.Companion.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NutritionScreen(
                        onBackClick = {
                            // TODO: Change this to navigate to appropriate screen instead of MainActivity
                            finish()
                        }
                    )
                }
            }
        }
    }
}