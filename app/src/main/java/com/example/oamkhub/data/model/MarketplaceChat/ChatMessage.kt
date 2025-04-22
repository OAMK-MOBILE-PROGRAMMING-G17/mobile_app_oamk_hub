package com.example.oamkhub.data.model.MarketplaceChat

data class ChatMessage(
    val _id: String?,
    val marketplace_id: String,
    val buyer_id: String,      // ← new field
    val user_id: String,
    val messages: String,
    val seen: Boolean,
    val created_at: String
)