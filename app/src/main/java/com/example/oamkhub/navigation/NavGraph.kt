package com.example.oamkhub.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.oamkhub.presentation.ui.screen.events.EventsScreen
import com.example.oamkhub.presentation.ui.screen.front.FrontScreen
import com.example.oamkhub.presentation.ui.screen.home.HomeScreen
import com.example.oamkhub.presentation.ui.screen.login.LoginScreen
import com.example.oamkhub.presentation.ui.screen.lostandfound.LostFoundScreen
import com.example.oamkhub.presentation.ui.screen.news.NewsScreen
import com.example.oamkhub.presentation.ui.screen.signup.SignupScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "lostfound") {
        composable("front") {
            FrontScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("signup") {
            SignupScreen(navController)
        }
        composable("home"){
            HomeScreen(navController)
        }
        composable("news"){
            NewsScreen(navController)
        }
        composable("events"){
            EventsScreen(navController)
        }

        composable("lostfound"){
            LostFoundScreen(navController)
        }
    }
}
