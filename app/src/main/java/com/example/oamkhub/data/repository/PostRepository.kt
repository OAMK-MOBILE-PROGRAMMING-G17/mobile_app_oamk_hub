package com.example.oamkhub.data.repository

import com.example.oamkhub.data.model.PostResponse
import com.example.oamkhub.data.network.RetrofitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class PostRepository {
    suspend fun createPost(token: String, content: String): Response<Map<String, Any>> {
        return withContext(Dispatchers.IO) {
            RetrofitInstance.api.createPost(
                token = "Bearer $token",
                request = mapOf("content" to content)
            )
        }
    }

    suspend fun fetchPosts(token: String): Response<List<PostResponse>> {
        return withContext(Dispatchers.IO) {
            RetrofitInstance.api.getPosts("Bearer $token")
        }
    }
}