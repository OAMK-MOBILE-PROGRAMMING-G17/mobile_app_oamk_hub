package com.example.oamkhub.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.presentation.ui.screen.marketplace.*
import com.example.oamkhub.viewmodel.MarketplaceViewModel
import java.net.URLDecoder

fun NavGraphBuilder.marketplaceGraph(navController: NavHostController) {
    navigation(
        route = "marketplace_root",
        startDestination = Routes.Marketplace.Root.route
    ) {
        composable(Routes.Marketplace.Root.route) {
            MarketplaceScreen(navController, viewModel())
        }

        composable(Routes.Marketplace.AddItem.route) {
            AddItemScreen(navController, viewModel())
        }

        composable(
            Routes.Marketplace.Detail.route,
            arguments = listOf(navArgument("itemId") { defaultValue = "" })
        ) { backStack ->
            val ctx = LocalContext.current
            val viewModel: MarketplaceViewModel = viewModel()
            val itemId = backStack.arguments?.getString("itemId") ?: ""
            val marketplaceItems by viewModel.marketplaceItems.collectAsState()

            LaunchedEffect(marketplaceItems.isEmpty()) {
                if (marketplaceItems.isEmpty()) {
                    UserPreferences(ctx).getToken()?.let(viewModel::fetchMarketplaceItems)
                }
            }

            val item = marketplaceItems.find { it.id == itemId }
            val token = UserPreferences(ctx).getToken().orEmpty()

            if (item != null)
                MarketplaceItemDetailScreen(navController, item, viewModel, token)
            else
                Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
        }

        composable(
            Routes.Marketplace.FullImage.route,
            arguments = listOf(
                navArgument("initialImageUrl") { defaultValue = "" },
                navArgument("encodedImages")  { defaultValue = "" }
            )
        ) { backStack ->
            val initial = URLDecoder.decode(backStack.arguments?.getString("initialImageUrl") ?: "", "UTF-8")
            val images = backStack.arguments?.getString("encodedImages")
                ?.split("||")
                ?.map { URLDecoder.decode(it, "UTF-8") }
                ?: emptyList()

            FullScreenImageView(navController, images, initial)
        }
    }
}
