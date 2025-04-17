package com.example.oamkhub.presentation.ui.screen.lostandfound

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.oamkhub.viewmodel.LostFoundViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.presentation.ui.components.BaseLayout

@Composable
fun LostFoundScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: LostFoundViewModel = viewModel()
    val lostItems by viewModel.lostProducts.collectAsState()
    var token by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val savedToken = UserPreferences(context).getToken()
        Log.d("LOST_FOUND", "Retrieved token: $savedToken")

        if (!savedToken.isNullOrEmpty()) {
            token = savedToken
            viewModel.fetchLostItems(savedToken)
        }
    }

    val displayedItems = lostItems.reversed()

    BaseLayout(
        navController = navController,
        title = "Lost & Found",
        isRefreshing = false,
        onRefresh = {
            if (token.isNotEmpty()) {
                viewModel.fetchLostItems(token)
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Button(
                onClick = { navController.navigate("lostfoundform") },
                modifier = Modifier.padding(16.dp)
            ) {
                Text("Lost something?")
            }

            LazyColumn {
                items(displayedItems) { item ->
                    item.id?.let {
                        LostFoundCard(
                            navController = navController,
                            item = item,
                            onAddComment = { comment ->
                                viewModel.addComment(it, comment, token)
                            }
                        )
                    } ?: Log.e("LOST_FOUND", "Lost item has null ID: $item")
                }
            }
        }
    }
}