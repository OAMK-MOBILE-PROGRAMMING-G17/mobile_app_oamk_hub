package com.example.oamkhub.data.network

import com.example.oamkhub.data.model.lostandfound.FoundComment
import com.example.oamkhub.data.model.lostandfound.FoundCommentRequest
import com.example.oamkhub.data.model.login.LoginRequest
import com.example.oamkhub.data.model.login.LoginResponse
import com.example.oamkhub.data.model.lostandfound.LostProduct
import com.example.oamkhub.data.model.lostandfound.LostProductRequest
import com.example.oamkhub.data.model.register.RegisterRequest
import com.example.oamkhub.data.model.register.RegisterResponse
import com.example.oamkhub.data.model.marketplace.MarketplaceItem
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path

interface ApiService {

    //user
    @POST("auth/register")
    suspend fun registerUser(@Body request: RegisterRequest): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun loginUser(@Body request: LoginRequest): Response<LoginResponse>

    @POST("auth/reset-password/request-otp")
    suspend fun requestOtp(@Body email: Map<String, String>): Response<Map<String, String>>

    @POST("auth/reset-password/verify-otp")
    suspend fun verifyOtp(@Body request: Map<String, String>): Response<Map<String, String>>

    @POST("auth/reset-password")
    suspend fun resetPassword(@Body request: Map<String, String>): Response<Map<String, String>>

    //Lost and Found
    @GET("/lost-products")
    suspend fun getAllLostProducts(@Header("Authorization") token: String): Response<List<LostProduct>>

    @GET("found-products/{lostProductId}")
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

    //Marketplace
    @GET("marketplace")
    suspend fun getAllMarketplaceItems(
        @Header("Authorization"
        ) token: String): Response<List<MarketplaceItem>>

    @Multipart
    @POST("marketplace")
    suspend fun createMarketplaceItem(
        @Header("Authorization") token: String,
        @Part ("title") title: RequestBody,
        @Part ("description") description: RequestBody,
        @Part ("price") price: RequestBody,
        @Part ("end_date") endDate: RequestBody,
        @Part images: List<MultipartBody.Part> // Array of image files
    ): Response<MarketplaceItem>

    @PUT("marketplace/{id}")
    suspend fun updateMarketplaceItem(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body item: MarketplaceItem
    ): Response<MarketplaceItem>

    @DELETE("marketplace/{id}")
    suspend fun deleteMarketplaceItem(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ResponseBody>
}
