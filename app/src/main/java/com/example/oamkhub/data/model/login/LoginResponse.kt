package com.example.oamkhub.data.model.login

import com.example.oamkhub.data.model.login.UserData
import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val message: String,
    val token: String?,
    val user: UserData
)