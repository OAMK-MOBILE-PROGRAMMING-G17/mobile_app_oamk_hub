package com.example.oamkhub.presentation.ui.screen.marketplace

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import androidx.compose.material3.MaterialTheme
import androidx.navigation.NavController
import com.example.oamkhub.data.model.marketplace.MarketplaceItem
import com.example.oamkhub.data.network.RetrofitInstance
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import com.example.oamkhub.presentation.ui.components.BaseLayout
import java.net.URLEncoder

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun MarketplaceItemDetailScreen(
    navController: NavController,
    item: MarketplaceItem
) {

    BaseLayout(navController = navController, title = item.title) { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)) {
            Text(text = item.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = item.description, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Price: \$${item.price}", style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "Offer Expires On: ${item.endDate}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            item.images.takeIf { it.isNotEmpty() }?.let { images ->
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(images) { imagePath ->
                        val fullImageUrl = "${RetrofitInstance.BASE_URL}/$imagePath".replace("\\", "/")
                        Image(
                            painter = rememberImagePainter(fullImageUrl),
                            contentDescription = "Marketplace item image",
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable {
                                    val encodedInitial = URLEncoder.encode(fullImageUrl, "UTF-8")
                                    val encodedList = images.joinToString("||") { URLEncoder.encode("${RetrofitInstance.BASE_URL}/$it".replace("\\", "/"), "UTF-8") }
                                    navController.navigate("fullscreen_image/$encodedInitial/$encodedList")
                                }
                        )
                    }
                }
            } ?: Text(text = "No images available")
        }
    }
}

