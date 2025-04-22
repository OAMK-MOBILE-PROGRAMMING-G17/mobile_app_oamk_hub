package com.example.oamkhub.data.model.marketplace

import com.google.gson.annotations.SerializedName

data class MarketplaceItem(
    @SerializedName("_id") val backendId: String, // Maps to "_id" in the JSON
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    @SerializedName("user_id") val userId: String? = null,
    val images: List<String>,
    val address: String,
    @SerializedName("gps_location") val gpsLocation: String, // Changed to String to match the backend
    @SerializedName("end_date") val endDate: String,
    val expired: Boolean,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)