package com.example.oamkhub.presentation.ui.screen.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.oamkhub.presentation.ui.components.BaseLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val isRefreshing = remember { mutableStateOf(false) }

    BaseLayout(
        navController = navController,
        title = "OAMK Hub",
        isRefreshing = isRefreshing.value,
        onRefresh = {
            isRefreshing.value = true
            kotlinx.coroutines.MainScope().launch {
                delay(1000)
                isRefreshing.value = false
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Welcome to the Home Screen 👋")
        }
    }
}