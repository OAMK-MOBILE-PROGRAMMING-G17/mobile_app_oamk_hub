package com.example.oamkhub.data.model.lostandfound

data class FoundComment(
    val id: String,
    val lostproducts_id: String,
    val comments: String,
    val user_id: String
)