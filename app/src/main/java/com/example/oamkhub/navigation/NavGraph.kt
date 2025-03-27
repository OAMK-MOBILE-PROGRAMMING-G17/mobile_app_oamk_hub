package com.example.oamkhub.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.oamkhub.presentation.ui.screen.front.FrontScreen
import com.example.oamkhub.presentation.ui.screen.home.HomeScreen
import com.example.oamkhub.presentation.ui.screen.login.LoginScreen
import com.example.oamkhub.presentation.ui.screen.signup.SignupScreen

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "home") {
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
    }
}
