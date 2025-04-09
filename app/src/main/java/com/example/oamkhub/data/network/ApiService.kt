package com.example.oamkhub.data.network

import com.example.oamkhub.data.model.FoundComment
import com.example.oamkhub.data.model.FoundCommentRequest
import com.example.oamkhub.data.model.LoginRequest
import com.example.oamkhub.data.model.LoginResponse
import com.example.oamkhub.data.model.LostProduct
import com.example.oamkhub.data.model.LostProductRequest
import com.example.oamkhub.data.model.RegisterRequest
import com.example.oamkhub.data.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @GET("/lost-products")
    suspend fun getAllLostProducts(@Header("Authorization") token: String): Response<List<LostProduct>>

    @GET("found/{lostProductId}")
    suspend fun getFoundComments(
        @Path("lostProductId") lostProductId: String,
        @Header("Authorization") token: String
    ): Response<List<FoundComment>>

    @POST("lost-products")
    suspend fun createLostProduct(
        @Header("Authorization") token: String,
        @Body request: LostProductRequest
    ): Response<Any>

    @POST("found-products")
    suspend fun createFoundComment(
        @Header("Authorization") token: String,
        @Body comment: FoundCommentRequest
    ): Response<Any>


}
