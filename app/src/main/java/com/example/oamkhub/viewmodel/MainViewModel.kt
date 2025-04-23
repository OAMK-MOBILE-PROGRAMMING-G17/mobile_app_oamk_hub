package com.example.oamkhub.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oamkhub.data.model.PostResponse
import com.example.oamkhub.data.model.UserInfo
import com.example.oamkhub.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class Post(val id: String, val author: String, val content: String, var likes: Int, var dislikes: Int)

class MainViewModel : ViewModel() {
    private val postRepository = PostRepository() // Direct instantiation

    private val _isPostSuccessful = MutableStateFlow(false)
    val isPostSuccessful: StateFlow<Boolean> = _isPostSuccessful

//    private val _posts = MutableStateFlow(
//        listOf(
//            Post("1", "User1", "This is the first post!", 10, 2),
//            Post("2", "User2", "Hello, world!", 5, 1),
//            Post("3", "User3", "Compose is amazing!", 20, 0)
//        )
//    )
//    val posts: StateFlow<List<Post>> = _posts

    private val _posts = MutableStateFlow<List<PostResponse>>(emptyList())
    val posts: StateFlow<List<PostResponse>> = _posts

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

//    fun likePost(postId: String) {
//        _posts.value = _posts.value.map {
//            if (it.id == postId) it.copy(likes = it.likes + 1) else it
//        }
//    }
//
//    fun dislikePost(postId: String) {
//        _posts.value = _posts.value.map {
//            if (it.id == postId) it.copy(dislikes = it.dislikes + 1) else it
//        }
//    }

    fun createPost(token: String, content: String) {
        viewModelScope.launch {
            try {
                val response = postRepository.createPost(token, content)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val newPost = PostResponse(
                            _id = responseBody["_id"] as String,
                            user_id = responseBody["user_id"] as String,
                            content = responseBody["content"] as String,
                            likes = (responseBody["likes"] as? Double)?.toInt() ?: 0,
                            dislikes = (responseBody["dislikes"] as? Double)?.toInt() ?: 0,
                            comments = (responseBody["comments"] as? Double)?.toInt() ?: 0,
                            created_at = responseBody["created_at"] as String,
                            updated_at = responseBody["updated_at"] as String,
                            user_info = UserInfo(
                                name = (responseBody["user_info"] as Map<*, *>)["name"] as String,
                                email = (responseBody["user_info"] as Map<*, *>)["email"] as String
                            )
                        )
                        // Add the new post to the top of the list
                        _posts.value = listOf(newPost) + _posts.value
                        _isPostSuccessful.value = true
                    } else {
                        _isPostSuccessful.value = false
                    }
                } else {
                    _isPostSuccessful.value = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _isPostSuccessful.value = false
            }
        }
    }

    fun fetchPosts(token: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = postRepository.fetchPosts(token)
                if (response.isSuccessful) {
                    _posts.value = response.body() ?: emptyList()
                } else {
                    _posts.value = emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _posts.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

}