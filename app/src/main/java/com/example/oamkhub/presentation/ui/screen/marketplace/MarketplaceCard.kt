package com.example.oamkhub.presentation.ui.screen.marketplace

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.oamkhub.data.model.marketplace.MarketplaceItem
import com.example.oamkhub.presentation.utils.getRelativeTimeSpan

@Composable
fun MarketplaceCard(
    item: MarketplaceItem,
    onItemClick: (String) -> Unit
) {
    val relativeTimeText = getRelativeTimeSpan(item.createdAt)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onItemClick(item.id) }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = relativeTimeText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(text = item.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = item.description, style = MaterialTheme.typography.bodyLarge)
            Text(text = "Price: € ${item.price}", style = MaterialTheme.typography.bodyLarge)

            item.images.forEach { image ->
                Image(painter = rememberAsyncImagePainter(image), contentDescription = "Item image")
            }
        }
    }
}