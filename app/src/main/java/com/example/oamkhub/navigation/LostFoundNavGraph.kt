package com.example.oamkhub.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.oamkhub.presentation.ui.screen.lostandfound.*
import com.example.oamkhub.viewmodel.LostFoundViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

fun NavGraphBuilder.lostFoundGraph(navController: NavHostController) {
    navigation(route = "lostfound_root", startDestination = Routes.LostFound.Root.route) {

        composable(Routes.LostFound.Root.route) {
            LostFoundScreen(navController)
        }

        composable(Routes.LostFound.Form.route) {
            LostFoundFormScreen(navController, viewModel())
        }

        composable(
            Routes.LostFound.Comments.route,
            arguments = listOf(
                navArgument("lostProductId") { defaultValue = "" },
                navArgument("title")         { defaultValue = "" }
            )
        ) { backStack ->
            LostFoundCommentsScreen(
                navController  = navController,
                viewModel      = viewModel<LostFoundViewModel>(),
                lostProductId  = backStack.arguments?.getString("lostProductId") ?: "",
                title          = backStack.arguments?.getString("title") ?: ""
            )
        }
    }
}
