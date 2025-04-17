package com.example.oamkhub.presentation.ui.screen.contacts

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ContactScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Emergency Contacts",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Phone Number
        Text(
            text = "Police: +1234567890",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+1234567890"))
                context.startActivity(intent)
            }
        )

        Text(
            text = "Fire Department: +0987654321",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+0987654321"))
                context.startActivity(intent)
            }
        )

        // Email
        Text(
            text = "Support: support@example.com",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:support@example.com")
                }
                context.startActivity(intent)
            }
        )

        Text(
            text = "Admin: admin@example.com",
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                textDecoration = TextDecoration.Underline
            ),
            modifier = Modifier.clickable {
                val intent = Intent(Intent.ACTION_SENDTO).apply {
                    data = Uri.parse("mailto:admin@example.com")
                }
                context.startActivity(intent)
            }
        )
    }
}