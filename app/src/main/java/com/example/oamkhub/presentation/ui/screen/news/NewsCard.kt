package com.example.oamkhub.presentation.ui.screen.news

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.oamkhub.data.model.news.NewsItem

@Composable
fun NewsCard(item: NewsItem) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.link))
                context.startActivity(intent)
            }
            .padding(4.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {

            item.imageUrl?.let { imageUrl ->
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(text = item.date, style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.title, style = MaterialTheme.typography.titleMedium)
        }
    }
}
