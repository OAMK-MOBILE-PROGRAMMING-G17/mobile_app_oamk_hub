package com.example.oamkhub.data.model.marketplace

import com.google.gson.annotations.SerializedName

data class MarketplaceItem(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    @SerializedName("end_date") val endDate: String,
    val images: List<String>,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("user_id") val userId: String? = null
)