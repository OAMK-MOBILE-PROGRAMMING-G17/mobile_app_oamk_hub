package com.example.oamkhub.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.oamkhub.presentation.ui.screen.events.EventsScreen
import com.example.oamkhub.presentation.ui.screen.front.FrontScreen
import com.example.oamkhub.presentation.ui.screen.home.HomeScreen
import com.example.oamkhub.presentation.ui.screen.login.LoginScreen
import com.example.oamkhub.presentation.ui.screen.lostandfound.LostFoundCommentsScreen
import com.example.oamkhub.presentation.ui.screen.lostandfound.LostFoundFormScreen
import com.example.oamkhub.presentation.ui.screen.lostandfound.LostFoundScreen
import com.example.oamkhub.presentation.ui.screen.news.NewsScreen
import com.example.oamkhub.presentation.ui.screen.signup.SignupScreen
import com.example.oamkhub.viewmodel.LostFoundViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val token = remember {
        com.example.oamkhub.data.utils.UserPreferences(context).getToken()
    }

    val startDestination = if (!token.isNullOrEmpty()) "home" else "login"

    NavHost(navController = navController, startDestination = startDestination) {
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

        composable("lostfoundform") {
            val viewModel: LostFoundViewModel = viewModel()
            LostFoundFormScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            route = "lostfoundcomments/{lostProductId}/{title}",
            arguments = listOf(
                navArgument("lostProductId") { type = NavType.StringType },
                navArgument("title") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val viewModel: LostFoundViewModel = viewModel()
            val lostProductId = backStackEntry.arguments?.getString("lostProductId") ?: ""
            val title = backStackEntry.arguments?.getString("title") ?: ""
            LostFoundCommentsScreen(
                navController = navController,
                viewModel = viewModel,
                lostProductId = lostProductId,
                title = title
            )
        }

    }
}
