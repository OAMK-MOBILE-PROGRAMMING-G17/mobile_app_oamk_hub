package com.example.oamkhub.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oamkhub.data.model.lostandfound.FoundComment
import com.example.oamkhub.data.model.lostandfound.FoundCommentRequest
import com.example.oamkhub.data.model.lostandfound.LostProduct
import com.example.oamkhub.data.network.RetrofitInstance
import com.example.oamkhub.data.repository.LostFoundRepository
import com.example.oamkhub.presentation.utils.uriToFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class LostFoundViewModel : ViewModel() {
    private val repository = LostFoundRepository()

    private val _lostProducts = MutableStateFlow<List<LostProduct>>(emptyList())
    val lostProducts: StateFlow<List<LostProduct>> = _lostProducts

    private val _selectedComments = MutableStateFlow<List<FoundComment>>(emptyList())
    val selectedComments: StateFlow<List<FoundComment>> = _selectedComments

    fun fetchLostItems(token: String) {
        viewModelScope.launch {
            val response = RetrofitInstance.api.getAllLostProducts("Bearer $token")
            if (response.isSuccessful) {
                val data = response.body() ?: emptyList()
                Log.d("LOST_FOUND", "Fetched ${data.size} items")
                _lostProducts.value = data
            } else {
                Log.e("LOST_FOUND", "Failed: ${response.errorBody()?.string()}")
            }
        }
    }

    fun fetchCommentsForLostItem(id: String, token: String) {
        viewModelScope.launch {
            val response = repository.getFoundComments(id, token)
            if (response.isSuccessful) {
                val comments = response.body() ?: emptyList()
                Log.d("COMMENTS_API", "Fetched ${comments.size} comments: $comments")
                _selectedComments.value = comments
            } else {
                Log.e("COMMENTS_API", "Failed to fetch comments: ${response.errorBody()?.string()}")
            }
        }
    }


    fun submitLostProduct(
        title: String,
        desc: String,
        location: String,
        time: String,
        token: String,
        imageUris: List<Uri>? = null,
        context: Context
    ) {
        viewModelScope.launch {
            try {
                val titlePart = title.toRequestBody("text/plain".toMediaTypeOrNull())
                val descPart = desc.toRequestBody("text/plain".toMediaTypeOrNull())
                val locationPart = location.toRequestBody("text/plain".toMediaTypeOrNull())
                val timePart = time.toRequestBody("text/plain".toMediaTypeOrNull())

                val imageParts = imageUris?.mapNotNull { uri ->
                    try {
                        val file = uriToFile(context, uri)
                        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                        MultipartBody.Part.createFormData("images", file.name, requestFile)
                    } catch (e: Exception) {
                        Log.e("LOST_FOUND", "Failed to convert uri: $uri", e)
                        null
                    }
                } ?: emptyList()

                val response = RetrofitInstance.api.createLostProductMultipart(
                    "Bearer $token",
                    titlePart,
                    descPart,
                    locationPart,
                    timePart,
                    imageParts
                )

                if (response.isSuccessful) {
                    Log.d("LOST_FOUND", "Lost item submitted successfully")
                    fetchLostItems(token)
                } else {
                    Log.e("LOST_FOUND", "Submission failed: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                Log.e("LOST_FOUND", "Exception during submission: ${e.message}")
            }
        }
    }

    fun addComment(lostProductId: String, comment: String, token: String) {
        viewModelScope.launch {
            val commentRequest = FoundCommentRequest(lostProductId, comment)
            val response = RetrofitInstance.api.createFoundComment("Bearer $token", commentRequest)
            if (response.isSuccessful) {
                fetchCommentsForLostItem(lostProductId, token)
            }
        }
    }
}
