package com.example.oamkhub

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.oamkhub.data.socket.MarketplaceChatSocket
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.navigation.AppNavGraph
import com.example.oamkhub.presentation.ui.theme.OAMKHubTheme
import com.example.oamkhub.viewmodel.ThemeViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            OAMKHubTheme {
                SocketController()
                val navController = rememberNavController()
                AppNavGraph(navController)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MarketplaceChatSocket.disconnect()
    }
}

@Composable
fun OAMKHubTheme(content: @Composable () -> Unit, themeViewModel: ThemeViewModel = viewModel()) {
    val isDarkModeEnabled by themeViewModel.isDarkModeEnabled.collectAsState()

    androidx.compose.material3.MaterialTheme(
        colorScheme = if (isDarkModeEnabled) darkColorScheme() else lightColorScheme()
    ) {
        content()
    }
}

@Composable
fun SocketController() {
    val context = LocalContext.current
    val prefs   = UserPreferences(context)

    // 1) Observe your stored token as a Flow
    val token by prefs
        .tokenFlow
        .collectAsState(initial = "")

    // 2) Whenever token changes, connect or disconnect
    LaunchedEffect(token) {
        if (token.isNotBlank()) {
            MarketplaceChatSocket.init(token)
            MarketplaceChatSocket.connect()
        } else {
            MarketplaceChatSocket.disconnect()
        }
    }
}