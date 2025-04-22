package com.example.oamkhub.data.repository

import com.example.oamkhub.data.model.MarketplaceChat.ChatMessage
import com.example.oamkhub.data.model.MarketplaceChat.UnifiedThread
import com.example.oamkhub.data.network.ChatApi
import com.example.oamkhub.data.socket.MarketplaceChatSocket
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.json.JSONObject
import kotlin.collections.orEmpty

class ChatRepository(
    private val api: ChatApi
) {
    // REST: get history for one listing + buyer
    suspend fun loadHistory(
        token: String,
        marketplaceId: String,
        buyerId: String
    ): List<ChatMessage> {
        val resp = api.getChatMessages("Bearer $token", marketplaceId, buyerId)
        return if (resp.isSuccessful) resp.body().orEmpty() else emptyList()
    }

    // REST: persist a new message
    suspend fun postMessage(
        token: String,
        marketplaceId: String,
        buyerId: String,
        text: String
    ) {
        api.createChatMessage(
            "Bearer $token",
            mapOf(
                "marketplace_id" to marketplaceId,
                "buyer_id"       to buyerId,
                "messages"       to text
            )
        )
    }

    suspend fun getAllChatThreads(token: String): List<UnifiedThread> {
        val res = api.getAllThreads("Bearer $token")
        return if (res.isSuccessful) res.body().orEmpty() else emptyList()
    }

    // SOCKET: live stream of ChatMessage
    fun subscribeRoom(
        marketplaceId: String,
        buyerId: String
    ): Flow<ChatMessage> = callbackFlow {
        MarketplaceChatSocket.joinRoom(marketplaceId, buyerId)
        val listener = MarketplaceChatSocket.onNewMessage { json ->
            trySend(json.toChatMessage())
        }
        awaitClose { MarketplaceChatSocket.offNewMessage(listener) }
    }

    // SOCKET: send a message
    fun sendSocketMessage(
        marketplaceId: String,
        buyerId: String,
        text: String,
        userId: String
    ) {
        MarketplaceChatSocket.sendChat(
            marketplaceId = marketplaceId,
            buyerId       = buyerId,
            message       = text,
            userId        = userId
        )
    }

    // JSON → model mapper
    private fun JSONObject.toChatMessage() = ChatMessage(
        _id            = optString("_id"),
        marketplace_id = getString("marketplace_id"),
        buyer_id       = optString("buyer_id"),
        user_id        = getString("user_id"),
        messages       = getString("messages"),
        seen           = optBoolean("seen", false),
        created_at     = getString("created_at")
    )
}