package com.example.oamkhub.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.presentation.ui.screen.about.AboutScreen
import com.example.oamkhub.presentation.ui.screen.home.HomeScreen
import com.example.oamkhub.presentation.ui.screen.main.MainScreen
import com.example.oamkhub.presentation.ui.screen.profile.ProfileScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController) {

    val context = LocalContext.current
    val token = remember { UserPreferences(context).getToken() }
    val startDest = if (token.isNullOrEmpty()) "auth" else "main"

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {
        authGraph(navController)
        resetGraph(navController)
        generalGraph(navController)
        lostFoundGraph(navController)
        marketplaceGraph(navController)
        composable("home") {
            HomeScreen(navController)
        }
        composable("main") {
            MainScreen(navController)
        }
        composable("about") {
            AboutScreen(navController)
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            }
        }
        composable("profile") {
            ProfileScreen(navController = navController)
        }
    }

}
