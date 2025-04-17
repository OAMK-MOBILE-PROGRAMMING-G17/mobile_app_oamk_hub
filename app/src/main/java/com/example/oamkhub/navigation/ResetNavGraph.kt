package com.example.oamkhub.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.oamkhub.presentation.ui.screen.reset.ChangePasswordScreen
import com.example.oamkhub.presentation.ui.screen.reset.OtpScreen
import com.example.oamkhub.presentation.ui.screen.reset.ResetEmailScreen

fun NavGraphBuilder.resetGraph(navController: NavHostController) {
    navigation(
        route = "reset",
        startDestination = Routes.Reset.Email.route
    ) {
        composable(Routes.Reset.Email.route) { ResetEmailScreen(navController) }

        composable(Routes.Reset.Otp.route,
            arguments = listOf(navArgument("email") { defaultValue = "" })
        ) { backStack ->
            OtpScreen(navController, backStack.arguments?.getString("email") ?: "")
        }

        composable(Routes.Reset.ChangePass.route,
            arguments = listOf(
                navArgument("email") { defaultValue = "" },
                navArgument("otp") { defaultValue = "" }
            )
        ) { backStack ->
            ChangePasswordScreen(
                navController,
                email = backStack.arguments?.getString("email") ?: "",
                otp   = backStack.arguments?.getString("otp") ?: ""
            )
        }
    }
}
