package com.example.oamkhub.presentation.ui.screen.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.oamkhub.R
import com.example.oamkhub.presentation.ui.components.BaseLayout

@Composable
fun ProfileScreen(navController: NavController, userName: String = "", userEmail: String = "") {
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profileImageUri = uri
    }

    BaseLayout(
        navController = navController,
        title = "Profile"
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Profile Picture and Info
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .clickable { imagePickerLauncher.launch("image/*") }
            ) {
                if (profileImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(profileImageUri),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile_placeholder),
                        contentDescription = "Default Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("John Doe", style = MaterialTheme.typography.titleLarge)
            Text("johndoe@example.com", style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(24.dp))

            // Profile Features
            ProfileFeatureItem(
                icon = Icons.Default.Edit,
                label = "Edit Profile",
                onClick = { /* Navigate to Edit Profile Screen */ }
            )
            ProfileFeatureItem(
                icon = Icons.Default.Lock,
                label = "Change Password",
                onClick = { /* Navigate to Change Password Screen */ }
            )
            ProfileFeatureItem(
                icon = Icons.Default.Person,
                label = "View Activity",
                onClick = { /* Navigate to Activity Screen */ }
            )
            ProfileFeatureItem(
                icon = Icons.Default.Settings,
                label = "Settings",
                onClick = { navController.navigate("settings") }
            )
            ProfileFeatureItem(
                icon = Icons.Default.ContactMail,
                label = "Contact Us",
                onClick = { navController.navigate("contact") }
            )
            ProfileFeatureItem(
                icon = Icons.Default.Info,
                label = "About Us",
                onClick = { navController.navigate("about") }
            )
            ProfileFeatureItem(
                icon = Icons.Default.Delete,
                label = "Delete Account",
                onClick = { /* Handle Delete Account Logic */ }
            )
            ProfileFeatureItem(
                icon = Icons.Default.ExitToApp,
                label = "Logout",
                onClick = { /* Handle Logout Logic */ }
            )
        }
    }
}

@Composable
fun ProfileFeatureItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
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
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}