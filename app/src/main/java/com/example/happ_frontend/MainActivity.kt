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
import com.example.happ_frontend.ui.screens.home.HomeScreen
import com.example.happ_frontend.ui.theme.HappfrontendTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HappfrontendTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { _ ->
                    Greeting("")
                    // ----------------------------------------------------------------

//                    Column(
//                        modifier = Modifier.fillMaxSize(),
//                        verticalArrangement = Arrangement.SpaceEvenly
//                    ) {
//                        Button(
//                            modifier = Modifier.align(Alignment.CenterHorizontally),
//                            onClick = {
//                                val intent = Intent(
//                                    this@MainActivity,
//                                    LoginActivity::class.java
//                                ).apply {
//                                    this.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
//                                            Intent.FLAG_ACTIVITY_NEW_TASK
//                                }
//                                startActivity(intent)
//                            }
//                        ) {
//                            Text("login")
//                        }
//
//                        Button(
//                            modifier = Modifier.align(Alignment.CenterHorizontally),
//                            onClick = {
//                                val intent = Intent(
//                                    this@MainActivity,
//                                    RegisterActivity::class.java
//                                ).apply {
//                                    this.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or
//                                            Intent.FLAG_ACTIVITY_NEW_TASK
//                                }
//                                startActivity(intent)
//                            }
//                        ) {
//                            Text("register")
//                        }
//                    }
                    // ------------------------------------------------------------------
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    HomeScreen()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HappfrontendTheme {
        Greeting("Test")
    }
}