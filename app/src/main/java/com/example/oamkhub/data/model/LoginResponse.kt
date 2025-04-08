package com.example.oamkhub.data.model

data class LoginResponse(
    val message: String,
    val token: String?,
    val user: UserData
)