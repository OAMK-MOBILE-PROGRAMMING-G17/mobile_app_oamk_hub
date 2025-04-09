package com.example.oamkhub.data.model

data class LostProductRequest(
    val title: String,
    val description: String,
    val location: String,
    val lost_time: String
)
