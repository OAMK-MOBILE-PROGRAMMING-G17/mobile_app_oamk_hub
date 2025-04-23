package com.example.oamkhub.presentation.ui.screen.contacts

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.oamkhub.presentation.ui.components.BaseLayout
import androidx.core.net.toUri

@Composable
fun ContactScreen(navController: androidx.navigation.NavController) {
    val context = LocalContext.current

    BaseLayout(
        navController = navController,
        title = "Emergency Contacts"
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            // Phone Number
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_DIAL, "tel:112".toUri())
                        context.startActivity(intent)
                    }
            ) {
                Icon(Icons.Default.Phone, contentDescription = "Phone Icon", modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Emergency number: 112",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_DIAL, "tel:029 5460211".toUri())
                        context.startActivity(intent)
                    }
            ) {
                Icon(Icons.Default.Phone, contentDescription = "Phone Icon", modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Oulu Police Station: 029 5460211",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_DIAL, "tel:116117".toUri())
                        context.startActivity(intent)
                    }
            ) {
                Icon(Icons.Default.Phone, contentDescription = "Phone Icon", modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Medical Helpline: 116117",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_DIAL, "tel:09 2525 0116".toUri())
                        context.startActivity(intent)
                    }
            ) {
                Icon(Icons.Default.Phone, contentDescription = "Phone Icon", modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Crisis Helpline: 09 2525 0116",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_DIAL, "044 703 8621".toUri())
                        context.startActivity(intent)
                    }
            ) {
                Icon(Icons.Default.Phone, contentDescription = "Phone Icon", modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Fire Department: 044 703 8621",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }

            // Email
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = "mailto:support@example.com".toUri()
                        }
                        context.startActivity(intent)
                    }
            ) {
                Icon(Icons.Default.Email, contentDescription = "Email Icon", modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Support: support@example.com",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            data = "mailto:admin@example.com".toUri()
                        }
                        context.startActivity(intent)
                    }
            ) {
                Icon(Icons.Default.Email, contentDescription = "Email Icon", modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Admin: admin@example.com",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        }
    }
}