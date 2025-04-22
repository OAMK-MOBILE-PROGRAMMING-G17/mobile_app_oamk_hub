package com.example.oamkhub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.oamkhub.data.model.MarketplaceChat.UnifiedThread
import com.example.oamkhub.data.repository.ChatRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.collections.filter

class ChatListViewModel(
    private val repo: ChatRepository,
    private val token: String,
    private val userId: String
) : ViewModel() {

    private val _threads = MutableStateFlow<List<UnifiedThread>>(emptyList())
    val threads: StateFlow<List<UnifiedThread>> = _threads

    init {
        viewModelScope.launch {
            val allThreads = repo.getAllChatThreads(token)
            _threads.value = allThreads.filter { it.participants.contains(userId) }
        }
    }

    class Factory(
        private val repo: ChatRepository,
        private val token: String,
        private val userId: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return ChatListViewModel(repo, token, userId) as T
        }
    }
}