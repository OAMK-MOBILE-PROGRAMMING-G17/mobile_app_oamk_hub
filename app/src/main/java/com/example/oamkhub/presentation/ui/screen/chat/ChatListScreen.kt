package com.example.oamkhub.presentation.ui.screen.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.oamkhub.data.network.RetrofitInstance
import com.example.oamkhub.data.repository.ChatRepository
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.presentation.ui.components.BaseLayout
import com.example.oamkhub.viewmodel.ChatListViewModel

@Composable
fun ChatListScreen(navController: NavController) {
    val ctx    = LocalContext.current
    val token  = UserPreferences(ctx).getToken().orEmpty()
    val userId = UserPreferences(ctx).getUserId().orEmpty()
    val repo   = remember { ChatRepository(RetrofitInstance.chatApi) }

    val vm: ChatListViewModel = viewModel(
        factory = ChatListViewModel.Factory(repo, token, userId)
    )

    val threads by vm.threads.collectAsState()

    BaseLayout(navController, title = "Conversations") { padding ->
        if (threads.isEmpty()) {
            Box(
                Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No conversations yet")
            }
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(threads) { thread ->
                    val other = thread.participants.first { it != userId }

                    ListItem(
                        headlineContent   = { Text("Item: ${thread.item_title}") },
                        supportingContent = { Text("Chat with: $other") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("chat/${thread.chatroom_id}/$token")
                            }
                            .padding(vertical = 8.dp)
                    )
                    Divider()
                }
            }
        }
    }
}