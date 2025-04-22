package com.example.oamkhub.presentation.ui.screen.marketplace

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.oamkhub.data.model.marketplace.MarketplaceItem
import com.example.oamkhub.presentation.utils.getRelativeTimeSpan

@Composable
fun MarketplaceCard(
    item: MarketplaceItem,
    onItemClick: (String) -> Unit
) {
    val context = LocalContext.current
    val relativeTimeText = getRelativeTimeSpan(item.createdAt)
    val baseUrl = "http://192.168.0.103:3003/"

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

            // Debugging: Log backend image paths
            Log.d("MarketplaceCard", "Backend image paths: ${item.images}")

            // Display images if available
//            item.images.forEach { image ->
//                val finalImageUrl = "$baseUrl${image.replace("\\", "/")}"
//
//                // Debugging: Log final image URL
//                Log.d("MarketplaceCard", "Final image URL: $finalImageUrl")
//
//                Image(
//                    painter = rememberAsyncImagePainter(
//                        model = finalImageUrl,
//                        onError = { error ->
//                            // Debugging: Log Coil image loading errors
//                            Log.e("MarketplaceCard", "Image loading error: ${error.result.throwable}")
//                        }
//                    ),
//                    contentDescription = "Item image",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp)
//                        .padding(top = 8.dp)
//                )
//            }

            // Display only the first image
            val firstImage = item.images.firstOrNull()
            if (!firstImage.isNullOrEmpty()) {
                val finalImageUrl = "$baseUrl${firstImage.replace("\\", "/")}"

                Image(
                    painter = rememberAsyncImagePainter(
                        model = finalImageUrl,
                        onError = { error ->
                            Log.e("MarketplaceCard", "Image loading error: ${error.result.throwable}")
                        }
                    ),
                    contentDescription = "Item image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(top = 8.dp)
                        .clip(MaterialTheme.shapes.medium) // Rounded corners
                        .aspectRatio(16f / 9f) // Maintain aspect ratio
                )
            }

            // "See Location" button
            Button(
                onClick = {
                    if (!item.gpsLocation.isNullOrEmpty()) {
                        val coordinates = item.gpsLocation.split(",")
                        if (coordinates.size == 2) {
                            val latitude = coordinates[0]
                            val longitude = coordinates[1]
                            val gmmIntentUri = Uri.parse("geo:$latitude,$longitude")
                            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                            if (mapIntent.resolveActivity(context.packageManager) != null) {
                                context.startActivity(mapIntent)
                            } else {
                                // Fallback: Open in any available map app
                                context.startActivity(Intent(Intent.ACTION_VIEW, gmmIntentUri))
                            }
                        } else {
                            Toast.makeText(context, "Invalid location format", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Location not available", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("See Location")
            }
        }
    }
}