package com.example.oamkhub.data.network

import com.example.oamkhub.data.model.MarketplaceChat.ChatMessage
import com.example.oamkhub.data.model.MarketplaceChat.UnifiedThread
import retrofit2.Response
import retrofit2.http.*

interface ChatApi {
    @GET("marketplace-chats/{marketplaceId}/{buyerId}")
    suspend fun getChatMessages(
        @Header("Authorization") token: String,
        @Path("marketplaceId") marketplaceId: String,
        @Path("buyerId") buyerId: String
    ): Response<List<ChatMessage>>

    @POST("marketplace-chats")
    suspend fun createChatMessage(
        @Header("Authorization") token: String,
        @Body body: Map<String, String>
    ): Response<Unit>

    @GET("marketplace-chats/chatthread")
    suspend fun getAllThreads(
        @Header("Authorization") token: String
    ): Response<List<UnifiedThread>>
}