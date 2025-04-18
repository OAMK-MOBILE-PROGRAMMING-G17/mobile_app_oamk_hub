package com.example.oamkhub.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class Post(val id: String, val author: String, val content: String, var likes: Int, var dislikes: Int)

class MainViewModel : ViewModel() {
    private val _posts = MutableStateFlow(
        listOf(
            Post("1", "User1", "This is the first post!", 10, 2),
            Post("2", "User2", "Hello, world!", 5, 1),
            Post("3", "User3", "Compose is amazing!", 20, 0)
        )
    )
    val posts: StateFlow<List<Post>> = _posts

    fun likePost(postId: String) {
        _posts.value = _posts.value.map {
            if (it.id == postId) it.copy(likes = it.likes + 1) else it
        }
    }

    fun dislikePost(postId: String) {
        _posts.value = _posts.value.map {
            if (it.id == postId) it.copy(dislikes = it.dislikes + 1) else it
        }
    }
}