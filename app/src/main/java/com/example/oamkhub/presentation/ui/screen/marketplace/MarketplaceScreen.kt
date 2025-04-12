package com.example.oamkhub.presentation.ui.screen.marketplace

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.presentation.ui.components.BaseLayout
import com.example.oamkhub.viewmodel.MarketplaceViewModel

@Composable
fun MarketplaceScreen(navController: NavController, viewModel: MarketplaceViewModel) {
    val marketplaceItems by viewModel.marketplaceItems.collectAsState(initial = emptyList())
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val token = UserPreferences(context).getToken()
        if (!token.isNullOrEmpty()) {
            viewModel.fetchMarketplaceItems(token)
        }
    }

    BaseLayout(navController = navController, title = "Marketplace") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Button(
                onClick = { navController.navigate("addItem") },
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                Text("Add New Item")
            }

            if (marketplaceItems.isEmpty()) {
                Text("No marketplace items available")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(marketplaceItems) { item ->
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
