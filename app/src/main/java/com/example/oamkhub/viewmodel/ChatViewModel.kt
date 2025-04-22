package com.example.oamkhub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.oamkhub.data.model.MarketplaceChat.ChatMessage
import com.example.oamkhub.data.repository.ChatRepository
import com.example.oamkhub.data.socket.MarketplaceChatSocket
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repo: ChatRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val marketplaceId = savedStateHandle["marketplaceId"] ?: ""
    val userId               = savedStateHandle["userId"]        ?: ""
    private val token        = savedStateHandle["token"]         ?: ""

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    init {
        // 1) Connect & join once
        MarketplaceChatSocket.joinRoom(marketplaceId, userId)

        // 2) Subscribe with de‑duplication
        repo.subscribeRoom(marketplaceId, userId)
            .onEach { incoming ->
                _messages.update { current ->
                    if (current.any { it._id == incoming._id }) current
                    else current + incoming
                }
            }
            .launchIn(viewModelScope)

        // 3) Load history once
        viewModelScope.launch {
            _messages.value = repo.loadHistory(token, marketplaceId, userId)
        }
    }

    fun send(text: String) {
        val trimmed = text.trim()
        if (trimmed.isBlank()) return

        // Real-time
        MarketplaceChatSocket.sendChat(
            marketplaceId = marketplaceId,
            buyerId       = userId,
            message       = trimmed,
            userId        = userId
        )

        // Persist
        viewModelScope.launch {
            repo.postMessage(token, marketplaceId, userId, trimmed)
        }
    }
}