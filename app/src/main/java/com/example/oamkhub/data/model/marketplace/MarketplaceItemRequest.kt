package com.example.oamkhub.data.model.marketplace

data class MarketplaceItemRequest(
    val title: String,
    val description: String,
    val price: String,
    val endDate: String,
    val images: List<String> // List of file paths of the uploaded images
)
