package com.example.oamkhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.oamkhub.navigation.AppNavGraph
import com.example.oamkhub.presentation.ui.theme.OAMKHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OAMKHubTheme {
                val navController = rememberNavController()
                AppNavGraph(navController = navController)
            }
        }
    }
}