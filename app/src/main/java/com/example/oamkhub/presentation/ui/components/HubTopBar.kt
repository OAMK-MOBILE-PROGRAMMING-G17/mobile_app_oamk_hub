package com.example.oamkhub.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.presentation.ui.theme.PrimaryOrange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HubTopBar(title: String, drawerState: DrawerState?, navController: NavController) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    TopAppBar(
        title = {
            Text(
                text = title,
                fontSize = 20.sp,
                color = Color.White
            )
        },
        navigationIcon = {
            if (drawerState != null) {
                IconButton(onClick = {
                    scope.launch { drawerState.open() }
                }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.White
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = {
                scope.launch {
                    UserPreferences(context).clearToken()
                    navController.navigate("front") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = PrimaryOrange)
    )
}
