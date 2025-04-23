package com.example.oamkhub.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.getValue

object RetrofitInstance {
    const val BASE_URL = "https://oamkhub-brdphqdjbbbyafag.northeurope-01.azurewebsites.net"

    // single Retrofit instance
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // REST endpoints
    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    // new chat endpoints
    val chatApi: ChatApi by lazy {
        retrofit.create(ChatApi::class.java)
    }
}
