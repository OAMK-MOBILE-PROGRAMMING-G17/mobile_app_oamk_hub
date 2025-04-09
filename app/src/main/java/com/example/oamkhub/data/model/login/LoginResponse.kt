package com.example.oamkhub.data.model.login

import com.example.oamkhub.data.model.UserData

data class LoginResponse(
    val message: String,
    val token: String?,
    val user: UserData
)