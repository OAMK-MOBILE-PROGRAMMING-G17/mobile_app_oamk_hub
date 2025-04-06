package com.example.oamkhub.data.network

import com.example.oamkhub.data.model.LoginRequest
import com.example.oamkhub.data.model.LoginResponse
import com.example.oamkhub.data.model.RegisterRequest
import com.example.oamkhub.data.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

}
