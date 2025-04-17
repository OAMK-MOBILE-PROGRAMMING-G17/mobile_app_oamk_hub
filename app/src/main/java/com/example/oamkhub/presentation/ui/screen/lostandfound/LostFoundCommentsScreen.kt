package com.example.oamkhub.presentation.ui.screen.lostandfound

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.oamkhub.data.network.RetrofitInstance
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.presentation.ui.components.BaseLayout
import com.example.oamkhub.viewmodel.LostFoundViewModel
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun LostFoundCommentsScreen(
    navController: NavController,
    viewModel: LostFoundViewModel,
    lostProductId: String,
    title: String
) {
    val context = LocalContext.current
    val lostProducts = viewModel.lostProducts.collectAsState().value
        .sortedByDescending { it.createdAt }

    val comments = viewModel.selectedComments.collectAsState().value
    val product = lostProducts.find { it.id == lostProductId }

    LaunchedEffect(lostProductId) {
        val token = UserPreferences(context).getToken()
        if (!token.isNullOrEmpty()) {
            viewModel.fetchLostItems(token)
            viewModel.fetchCommentsForLostItem(lostProductId, token)
        }
    }

    BaseLayout(navController = navController, title = "Comments: $title") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            product?.let {
                Text(text = it.title, style = MaterialTheme.typography.titleLarge)

                val postedAgoText = it.createdAt?.let { isoTime ->
                    try {
                        val postTime = ZonedDateTime.parse(isoTime, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
                        val now = ZonedDateTime.now()
                        val duration = Duration.between(postTime, now)

                        if (duration.toHours() < 24) {
                            "Posted ${duration.toHours()} hour(s) ago"
                        } else null
                    } catch (e: Exception) {
                        null
                    }
                }

                postedAgoText?.let { timeStr ->
                    Text(text = timeStr, style = MaterialTheme.typography.labelMedium)
                }

                Text(text = "Description: ${it.description}")
                Text(text = "Location: ${it.location}")
                Text(text = "Lost At: ${it.lostTime}")
                Spacer(modifier = Modifier.height(12.dp))

                if (it.images.isNotEmpty()) {
                    Text("Images:", style = MaterialTheme.typography.titleMedium)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 500.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(it.images) { imagePath ->
                            val fullUrl = "${RetrofitInstance.BASE_URL}/$imagePath".replace("\\", "/")
                            Image(
                                painter = rememberAsyncImagePainter(fullUrl),
                                contentDescription = "Lost item image",
                                modifier = Modifier
                                    .aspectRatio(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (comments.isEmpty()) {
                Text("No comments yet.")
            } else {
                Text("Comments", style = MaterialTheme.typography.titleMedium)
                comments.forEach {
                    Text("- ${it.comments}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Back to Lost & Found")
            }
        }
    }
}
