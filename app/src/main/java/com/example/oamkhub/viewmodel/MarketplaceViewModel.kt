package com.example.oamkhub.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oamkhub.data.model.marketplace.MarketplaceItem
import com.example.oamkhub.data.network.RetrofitInstance
import com.example.oamkhub.data.repository.MarketplaceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import com.example.oamkhub.presentation.utils.uriToFile

class MarketplaceViewModel : ViewModel() {

    private val marketplaceRepository = MarketplaceRepository()

    private val _marketplaceItems = MutableStateFlow<List<MarketplaceItem>>(emptyList())
    val marketplaceItems: StateFlow<List<MarketplaceItem>> = _marketplaceItems

    // Fetch all marketplace items
    fun fetchMarketplaceItems(token: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getAllMarketplaceItems("Bearer $token")
                if (response.isSuccessful) {
                    _marketplaceItems.value = response.body() ?: emptyList()
                    Log.d("Marketplace", "Fetched Items: ${_marketplaceItems.value}")
                } else {
                    Log.e("Marketplace", "Error fetching items")
                }
            } catch (e: Exception) {
                Log.e("Marketplace", "Error fetching items: $e")
            }
        }
    }



    // Create a new marketplace item
    fun createMarketplaceItem(
        context: Context,
        token: String,
        title: String,
        description: String,
        price: String,
        endDate: String,
        images: List<Uri>,
        address: String,
        gpsLocation: Pair<Double, Double>?
    ) {
        viewModelScope.launch {
            val titleRequestBody = title.toRequestBody("text/plain".toMediaTypeOrNull())
            val descriptionRequestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())
            val priceRequestBody = price.toRequestBody("text/plain".toMediaTypeOrNull())
            val endDateRequestBody = endDate.toRequestBody("text/plain".toMediaTypeOrNull())
            val addressRequestBody = address.toRequestBody("text/plain".toMediaTypeOrNull())
            val gpsLocationRequestBody = gpsLocation?.let {
                "${it.first},${it.second}".toRequestBody("text/plain".toMediaTypeOrNull())
            }

            val imageParts = images.map { uri ->
                val file = uriToFile(context, uri)
                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                MultipartBody.Part.createFormData("images", file.name, requestFile)
            }

            val item = marketplaceRepository.createMarketplaceItem(
                token,
                titleRequestBody,
                descriptionRequestBody,
                priceRequestBody,
                endDateRequestBody,
                addressRequestBody,
                gpsLocationRequestBody,
                imageParts
            )

            if (item != null) {
                fetchMarketplaceItems(token)
            }
        }
    }

    fun deleteMarketplaceItem(itemId: String, token: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.deleteMarketplaceItem("Bearer $token", itemId)
                if (response.isSuccessful) {
                    Log.d("DELETE", "Success: Item deleted")
                    onSuccess()
                } else {
                    Log.e("DELETE", "Error: ${response.code()} - ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("DELETE", "Exception during delete: ${e.localizedMessage}")
            }
        }
    }
}