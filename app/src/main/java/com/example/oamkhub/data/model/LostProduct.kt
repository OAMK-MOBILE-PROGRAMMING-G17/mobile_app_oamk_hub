package com.example.oamkhub.data.model

import com.google.gson.annotations.SerializedName

data class LostProduct(
    @SerializedName("_id") val id: String?,
    val title: String,
    val description: String,
    val location: String,
    @SerializedName("lost_time") val lostTime: String,
    val images: List<String>,
    @SerializedName("user_id") val userId: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)
