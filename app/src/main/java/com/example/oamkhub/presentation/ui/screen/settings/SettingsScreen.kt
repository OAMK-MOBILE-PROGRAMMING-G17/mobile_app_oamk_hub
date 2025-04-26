package com.example.oamkhub.presentation.ui.screen.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.oamkhub.presentation.ui.components.BaseLayout
import com.example.oamkhub.viewmodel.ThemeViewModel

@Composable
fun SettingsScreen(navController: NavController, themeViewModel: ThemeViewModel = viewModel()) {
    val isDarkModeEnabled by themeViewModel.isDarkModeEnabled.collectAsState()
    var areNotificationsEnabled by remember { mutableStateOf(false) } // Add this line

    BaseLayout(
        navController = navController,
        title = "Settings"
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Dark Mode Toggle
            SettingsOptionItem(
                icon = Icons.Default.Brightness4,
                label = "Dark Mode",
                trailingContent = {
                    Switch(
                        checked = isDarkModeEnabled,
                        onCheckedChange = { themeViewModel.toggleDarkMode(it) }
                    )
                }
            )
            // Notifications Toggle
            SettingsOptionItem(
                icon = Icons.Default.Notifications,
                label = "Notifications",
                trailingContent = {
                    Switch(
                        checked = areNotificationsEnabled,
                        onCheckedChange = { areNotificationsEnabled = it }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Account Settings Navigation
            SettingsOptionItem(
                icon = Icons.Default.AccountCircle,
                label = "Account Settings",
                onClick = { navController.navigate("accountSettings") }
            )

            // Privacy Policy Navigation
            SettingsOptionItem(
                icon = Icons.Default.PrivacyTip,
                label = "Privacy Policy",
                onClick = { navController.navigate("privacyPolicy") }
            )
        }
    }
}

@Composable
fun SettingsOptionItem(
    icon: ImageVector,
    label: String,
    trailingContent: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(enabled = onClick != null, onClick = { onClick?.invoke() })
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        trailingContent?.invoke()
    }
}