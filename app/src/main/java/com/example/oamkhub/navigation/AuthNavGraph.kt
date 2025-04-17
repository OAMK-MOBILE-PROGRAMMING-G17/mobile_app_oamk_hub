package com.example.oamkhub.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.oamkhub.presentation.ui.screen.front.FrontScreen
import com.example.oamkhub.presentation.ui.screen.login.LoginScreen
import com.example.oamkhub.presentation.ui.screen.signup.SignupScreen

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        route = "auth",
        startDestination = Routes.Auth.Login.route
    ) {

        composable(Routes.Auth.Front.route)  { FrontScreen(navController) }
        composable(Routes.Auth.Login.route)  { LoginScreen(navController) }
        composable(Routes.Auth.Signup.route) { SignupScreen(navController) }
    }
}
