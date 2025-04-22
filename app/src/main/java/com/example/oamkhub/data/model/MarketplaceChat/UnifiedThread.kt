package com.example.oamkhub.data.model.MarketplaceChat

data class UnifiedThread(
    val chatroom_id: String,
    val marketplace_id: String,
    val buyer_id: String,
    val seller_id: String,
    val participants: List<String>,
    val item_title: String?,
    val seen: Boolean = false,
    val unread_count: Int = 0
)