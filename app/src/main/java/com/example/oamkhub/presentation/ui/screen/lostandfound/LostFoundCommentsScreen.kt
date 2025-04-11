package com.example.oamkhub.presentation.ui.screen.lostandfound

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.presentation.ui.components.BaseLayout
import com.example.oamkhub.viewmodel.LostFoundViewModel

@Composable
fun LostFoundCommentsScreen(
    navController: NavController,
    viewModel: LostFoundViewModel,
    lostProductId: String,
    title: String
) {
    val context = LocalContext.current
    val lostProduct = viewModel.lostProducts.collectAsState(initial = listOf()).value
    val selectedCommentsState = viewModel.selectedComments.collectAsState()
    val selectedComments = selectedCommentsState.value

    LaunchedEffect(lostProductId) {
        val token = UserPreferences(context).getToken()
        if (!token.isNullOrEmpty()) {
            viewModel.fetchCommentsForLostItem(lostProductId, token)
            viewModel.fetchLostItems(token)
        }
    }
    val productDetails = lostProduct.find { it.id == lostProductId }

    BaseLayout(navController = navController, title = "Comments: $title") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            productDetails?.let { product ->
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
            )
                Text(text = "Description: ${product.description}")
                Text(text = "Location: ${product.location}")
                Text(text = "Lost At: ${product.lostTime}")
            }

            Spacer(modifier = Modifier.height(20.dp))

            if (selectedComments.isEmpty()) {
                Text("No comments yet.")
            } else {
                Text("Comments", style = MaterialTheme.typography.titleMedium)
                selectedComments.forEach {
                    Text("- ${it.comments}", style = MaterialTheme.typography.bodyMedium)
                }
            }

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Back to Lost & Found")
            }
        }
    }
}
