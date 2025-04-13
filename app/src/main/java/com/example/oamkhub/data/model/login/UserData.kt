package com.example.oamkhub.data.model.login

import com.google.gson.annotations.SerializedName

data class UserData(
    @SerializedName("_id") val id: String,
    val name: String,
    val email: String
)