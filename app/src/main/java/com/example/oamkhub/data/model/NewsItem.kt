package com.example.oamkhub.data.model

data class NewsItem(
    val title: String,
    val link: String,
    val date: String,
    val imageUrl: String? = null
)