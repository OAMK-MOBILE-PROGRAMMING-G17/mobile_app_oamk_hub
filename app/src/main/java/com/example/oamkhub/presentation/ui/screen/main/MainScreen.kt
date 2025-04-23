package com.example.oamkhub.presentation.ui.screen.main

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.presentation.ui.components.BaseLayout
import com.example.oamkhub.viewmodel.MainViewModel
import com.example.oamkhub.viewmodel.Post

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = viewModel()) {
    val posts by viewModel.posts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isPostSuccessful by viewModel.isPostSuccessful.collectAsState()
    var postText by remember { mutableStateOf("") }
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE) }
//    val token = sharedPreferences.getString("auth_token", null) ?: ""
    val token = UserPreferences(context).getToken().orEmpty()

    LaunchedEffect(Unit) {
        if (token.isNotEmpty()) {
            viewModel.fetchPosts(token)
        } else {
            Toast.makeText(context, "Token not found. Please log in.", Toast.LENGTH_SHORT).show()
        }
    }

    BaseLayout(
        navController = navController,
        title = "Main Page",
        showDrawer = true
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Editable text field for creating a new post
            OutlinedTextField(
                value = postText,
                onValueChange = { postText = it },
                placeholder = { Text("What's on your mind?") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary,
                    focusedBorderColor = MaterialTheme.colorScheme.primary
                ),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send, // Use a "Send" icon
                        contentDescription = "Send Post",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                if (postText.isNotBlank()) {
                                    if (token.isNotEmpty()) {
                                        viewModel.createPost(token, postText)
                                        postText = "" // Clear the field after submission
                                    } else {
                                        Toast.makeText(context, "Token not found. Please log in.", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                    )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(posts) { post ->
                        PostItem(
                            post = Post(
                                id = post._id,
                                author = post.user_info.name,
                                content = post.content,
                                likes = post.likes,
                                dislikes = post.dislikes
                            ),
                            onLike = { /* Handle like */ },
                            onDislike = { /* Handle dislike */ },
                            onComment = { /* Handle comment */ }
                        )
                    }
                }
            }



//            LazyColumn(
//                modifier = Modifier.fillMaxSize(),
//                verticalArrangement = Arrangement.spacedBy(16.dp)
//            ) {
//                items(posts) { post ->
//                    PostItem(
//                        post = post,
//                        onLike = { viewModel.likePost(post.id) },
//                        onDislike = { viewModel.dislikePost(post.id) },
//                        onComment = { /* Navigate to comment screen */ }
//                    )
//                }
//            }

        }
    }

}

@Composable
fun PostItem(
    post: Post,
    onLike: () -> Unit,
    onDislike: () -> Unit,
    onComment: () -> Unit
) {
    var isLiked by remember { mutableStateOf(false) }
    var isDisliked by remember { mutableStateOf(false) }
    var showCommentDialog by remember { mutableStateOf(false) }
    var commentText by remember { mutableStateOf("") }
    val comments = remember { mutableStateListOf<String>() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = post.author,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        isLiked = !isLiked
                        if (isLiked) {
                            isDisliked = false
                            onLike()
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.ThumbUp,
                        contentDescription = "Like",
                        tint = if (isLiked) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                }
                IconButton(
                    onClick = {
                        isDisliked = !isDisliked
                        if (isDisliked) {
                            isLiked = false
                            onDislike()
                        }
                    }
                ) {
                    Icon(
                        Icons.Default.ThumbDown,
                        contentDescription = "Dislike",
                        tint = if (isDisliked) MaterialTheme.colorScheme.error else Color.Gray
                    )
                }
                IconButton(onClick = { showCommentDialog = true }) {
                    Icon(Icons.Default.Comment, contentDescription = "Comment", tint = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }

    if (showCommentDialog) {
        AlertDialog(
            onDismissRequest = { showCommentDialog = false },
            title = { Text("Comments") },
            text = {
                Column {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        items(comments) { comment ->
                            Text(
                                text = comment,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = commentText,
                        onValueChange = { commentText = it },
                        placeholder = { Text("Write a comment...") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            comments.add(commentText)
                            commentText = ""
                        }
                    }
                ) {
                    Text("Add Comment")
                }
            },
            dismissButton = {
                TextButton(onClick = { showCommentDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}