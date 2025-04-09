package com.example.oamkhub.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oamkhub.data.model.lostandfound.FoundComment
import com.example.oamkhub.data.model.lostandfound.FoundCommentRequest
import com.example.oamkhub.data.model.lostandfound.LostProduct
import com.example.oamkhub.data.model.lostandfound.LostProductRequest
import com.example.oamkhub.data.network.RetrofitInstance
import com.example.oamkhub.data.repository.LostFoundRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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
                _selectedComments.value = response.body() ?: emptyList()
            }
        }
    }

    fun submitLostProduct(title: String, desc: String, location: String, time: String, token: String) {
        viewModelScope.launch {
            val lost = LostProductRequest(title, desc, location, time)
            val response = RetrofitInstance.api.createLostProduct("Bearer $token", lost)
            if (response.isSuccessful) {
                fetchLostItems(token)
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
