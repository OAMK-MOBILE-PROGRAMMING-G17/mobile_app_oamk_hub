package com.example.oamkhub.presentation.ui.screen.marketplace

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.navigation.NavController
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.presentation.ui.components.BaseLayout
import com.example.oamkhub.viewmodel.MarketplaceViewModel

@Composable
fun MarketplaceScreen(navController: NavController, viewModel: MarketplaceViewModel) {
    val marketplaceItems by viewModel.marketplaceItems.collectAsState(initial = emptyList())
    val context = LocalContext.current

    LaunchedEffect(key1 = marketplaceItems.size) {
        val token = UserPreferences(context).getToken()
        if (!token.isNullOrEmpty()) {
            viewModel.fetchMarketplaceItems(token)
        }
    }

    var showOnlyMyListings by remember { mutableStateOf(false) }
    val userIdState = remember { mutableStateOf<String?>(null) }
    LaunchedEffect(Unit) {
        userIdState.value = UserPreferences(context).getUserId()
    }

    val displayedItems = if (showOnlyMyListings && userIdState.value != null) {
        marketplaceItems.filter { it.userId == userIdState.value }.reversed()
    } else {
        marketplaceItems.reversed()
    }

    BaseLayout(
        navController = navController,
        title = "Marketplace",
        showDrawer = true,
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Sell,
                            contentDescription = "Sell Icon"
                        )
                    },
                    label = {Text("Marketplace")},
                    selected = !showOnlyMyListings,
                    onClick = {
                        showOnlyMyListings = false
                    }
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Reorder,
                            contentDescription = "Reorder Icon"
                        )
                           },
                    label = {Text("My Listing")},
                    selected = showOnlyMyListings,
                    onClick = {
                        showOnlyMyListings = true
                    }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
        ) {
            Button(
                onClick = { navController.navigate("addItem") },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Add New Item")
            }

            if (displayedItems.isEmpty()) {
                Text("No marketplace items available")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(displayedItems) { item ->
                        MarketplaceCard(
                            item = item,
                            onItemClick = {
                                navController.navigate("marketplaceDetail/${item.id}")
                            }
                        )
                    }
                }
            }
        }
    }
}
