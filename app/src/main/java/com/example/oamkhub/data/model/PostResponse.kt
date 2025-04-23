package com.example.oamkhub.data.model

data class PostResponse(
    val _id: String,
    val user_id: String,
    val content: String,
    val likes: Int,
    val dislikes: Int,
    val comments: Int,
    val created_at: String,
    val updated_at: String,
    val user_info: UserInfo
)

data class UserInfo(
    val name: String,
    val email: String
)