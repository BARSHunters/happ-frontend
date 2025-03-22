package com.example.happ_frontend

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.happ_frontend.ui.screens.weight.WeightHistoryScreen
import com.example.happ_frontend.ui.theme.HappfrontendTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappfrontendTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {  _ ->
                    Greeting("")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    WeightHistoryScreen()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HappfrontendTheme {
        Greeting("Test")
    }
}