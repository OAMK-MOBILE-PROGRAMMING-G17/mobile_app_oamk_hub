package com.example.oamkhub.presentation.ui.screen.news

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.oamkhub.presentation.ui.components.BaseLayout
import com.example.oamkhub.viewmodel.NewsViewModel

@Composable
fun NewsScreen(navController: androidx.navigation.NavController) {
    val viewModel = remember { NewsViewModel() }
    val newsList by viewModel.news.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadNews()
    }

    BaseLayout(
        navController = navController,
        title = "News",
        isRefreshing = false,
        onRefresh = {
            viewModel.loadNews()
        }
    ) { padding ->
        if (newsList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(newsList) { item ->
                    NewsCard(item)
                }
            }
        }
    }
}
