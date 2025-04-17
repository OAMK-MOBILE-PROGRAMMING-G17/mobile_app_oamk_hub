package com.example.oamkhub.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.presentation.ui.screen.contacts.ContactScreen
import com.example.oamkhub.presentation.ui.screen.events.EventsScreen
import com.example.oamkhub.presentation.ui.screen.front.FrontScreen
import com.example.oamkhub.presentation.ui.screen.home.HomeScreen
import com.example.oamkhub.presentation.ui.screen.login.LoginScreen
import com.example.oamkhub.presentation.ui.screen.lostandfound.LostFoundCommentsScreen
import com.example.oamkhub.presentation.ui.screen.lostandfound.LostFoundFormScreen
import com.example.oamkhub.presentation.ui.screen.lostandfound.LostFoundScreen
import com.example.oamkhub.presentation.ui.screen.marketplace.AddItemScreen
import com.example.oamkhub.presentation.ui.screen.marketplace.FullScreenImageView
import com.example.oamkhub.presentation.ui.screen.marketplace.MarketplaceItemDetailScreen
import com.example.oamkhub.presentation.ui.screen.marketplace.MarketplaceScreen
import com.example.oamkhub.presentation.ui.screen.news.NewsScreen
import com.example.oamkhub.presentation.ui.screen.reset.ChangePasswordScreen
import com.example.oamkhub.presentation.ui.screen.reset.OtpScreen
import com.example.oamkhub.presentation.ui.screen.reset.ResetEmailScreen
import com.example.oamkhub.presentation.ui.screen.signup.SignupScreen
import com.example.oamkhub.viewmodel.LostFoundViewModel
import com.example.oamkhub.viewmodel.MarketplaceViewModel
import java.net.URLDecoder

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val token = remember {
        com.example.oamkhub.data.utils.UserPreferences(context).getToken()
    }

    val startDestination = if (!token.isNullOrEmpty()) "marketplace" else "login"

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

        composable("resetEmail") { ResetEmailScreen(navController) }
        composable("otpScreen/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            OtpScreen(navController, email)
        }
        composable("changePassword/{email}/{otp}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email") ?: ""
            val otp = backStackEntry.arguments?.getString("otp") ?: ""
            ChangePasswordScreen(navController, email, otp)
        }

        composable("home") {
            HomeScreen(navController)
        }
        composable("news") {
            NewsScreen(navController)
        }
        composable("events") {
            EventsScreen(navController)
        }

        composable("lostfound") {
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

        composable("marketplace") {
            val viewModel: MarketplaceViewModel = viewModel()
            MarketplaceScreen(navController = navController, viewModel = viewModel)
        }

        composable("addItem") {
            val viewModel: MarketplaceViewModel = viewModel()
            AddItemScreen(navController = navController, viewModel = viewModel)
        }

        composable(
            route = "marketplaceDetail/{itemId}",
            arguments = listOf(navArgument("itemId") { type = NavType.StringType })
        ) { backStackEntry ->
            val context = LocalContext.current
            val viewModel: MarketplaceViewModel = viewModel()
            val itemId = backStackEntry.arguments?.getString("itemId") ?: ""
            val marketplaceItems by viewModel.marketplaceItems.collectAsState()

            LaunchedEffect(key1 = marketplaceItems.isEmpty()) {
                if (marketplaceItems.isEmpty()) {
                    val token = UserPreferences(context).getToken()
                    if (!token.isNullOrEmpty()) {
                        viewModel.fetchMarketplaceItems(token)
                    }
                }
            }

            val item = marketplaceItems.find { it.id == itemId }
            val token = UserPreferences(LocalContext.current).getToken() ?: ""

            if (item != null) {
                MarketplaceItemDetailScreen(navController = navController, item = item, token=token)
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
        }

        composable(
            route = "fullscreen_image/{initialImageUrl}/{encodedImages}",
            arguments = listOf(
                navArgument("initialImageUrl") { type = NavType.StringType },
                navArgument("encodedImages") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val initialImageUrl = URLDecoder.decode(backStackEntry.arguments?.getString("initialImageUrl") ?: "", "UTF-8")
            val encodedImages = backStackEntry.arguments?.getString("encodedImages") ?: ""
            val images = encodedImages.split("||").map { URLDecoder.decode(it, "UTF-8") }

            FullScreenImageView(navController, images, initialImageUrl)
        }


        composable("contact") {
            ContactScreen()
        }





    }
}
