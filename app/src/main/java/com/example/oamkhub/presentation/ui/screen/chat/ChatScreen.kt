package com.example.oamkhub.presentation.ui.screen.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.compose.ui.text.input.ImeAction
import com.example.oamkhub.data.network.RetrofitInstance
import com.example.oamkhub.data.repository.ChatRepository
import com.example.oamkhub.data.model.MarketplaceChat.ChatMessage
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.presentation.ui.components.BaseLayout
import com.example.oamkhub.viewmodel.ChatViewModel

@Composable
fun ChatScreen(
    navController: NavController,
    chatroomId: String,
    token: String
) {
    // Build ViewModel with our nav-args
    val (marketplaceId, buyerId) = remember(chatroomId) {
        chatroomId.split("_").let {
            it[0] to it[1]
        }
    }
    val context = LocalContext.current
    val currentUserId = UserPreferences(context).getUserId().orEmpty()

    val repo = remember { ChatRepository(RetrofitInstance.chatApi) }

    val savedStateHandle = remember {
        SavedStateHandle(
            mapOf(
                "marketplaceId" to marketplaceId,
                "userId"        to buyerId,
                "token"         to token
            )
        )
    }
    val viewModel = remember { ChatViewModel(repo, savedStateHandle) }

    val messages by viewModel.messages.collectAsState()
    var text by rememberSaveable { mutableStateOf("") }

    BaseLayout(navController = navController, title = "Chat") { padding ->
        Scaffold(
            bottomBar = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 40.dp)
                ) {
                    OutlinedTextField(
                        value = text,
                        onValueChange = { text = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Type a message…") },
                        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
                        keyboardActions = KeyboardActions(
                            onSend = {
                                if (text.isNotBlank()) {
                                    viewModel.send(text)
                                    text = ""
                                }
                            }
                        )
                    )

                    Spacer(Modifier.width(8.dp))

                    Button(onClick = {
                        if (text.isNotBlank()) {
                            viewModel.send(text)
                            text = ""
                        }
                    }) {
                        Text("Send", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            }
        ) { innerPadding ->
            LazyColumn(
                reverseLayout = false,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(innerPadding),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.Bottom

            ) {
                items(messages) { msg ->
                    MessageBubble(
                        message = msg,
                        isMine  = msg.user_id == currentUserId
                    )
                }
            }
        }
    }
}

@Composable
private fun MessageBubble(message: ChatMessage, isMine: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = if (isMine) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 2.dp,
            color = if (isMine)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        ) {
            Text(
                text = message.messages,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}