package com.example.oamkhub.data.repository

import android.util.Log
import com.example.oamkhub.data.model.marketplace.MarketplaceItem
import com.example.oamkhub.data.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class MarketplaceRepository {

    // Fetch all marketplace items
    suspend fun fetchMarketplaceItems(token: String): List<MarketplaceItem> {
        return withContext(Dispatchers.IO) {
            val response = RetrofitInstance.api.getAllMarketplaceItems("Bearer $token")
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList() // Handle failure, maybe throw an exception
            }
        }
    }

    // Create a new marketplace item
    sealed class MarketplaceResult {
        data class Success(val item: MarketplaceItem) : MarketplaceResult()
        data class Error(val message: String) : MarketplaceResult()
    }

    suspend fun createMarketplaceItem(
        token: String,
        title: RequestBody,
        description: RequestBody,
        price: RequestBody,
        endDate: RequestBody,
        address: RequestBody,
        gpsLocation: RequestBody?,
        images: List<MultipartBody.Part>
    ): MarketplaceItem? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitInstance.api.createMarketplaceItem(
                    "Bearer $token",
                    title,
                    description,
                    price,
                    endDate,
                    address,
                    gpsLocation ?: "".toRequestBody("text/plain".toMediaTypeOrNull()),
                    images
                )
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("MarketplaceRepo", "Failed: ${response.code()} ${response.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("MarketplaceRepo", "Exception: ${e.message}")
                null
            }
        }
    }
}
