package com.example.oamkhub.presentation.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.launch

@Composable
fun DrawerContent(navController: NavController, drawerState: DrawerState) {
    val scope = rememberCoroutineScope()

    ModalDrawerSheet {
        Spacer(modifier = Modifier.height(12.dp))

        NavigationDrawerItem(
            label = { Text("Home") },
            selected = false,
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            onClick = {
                scope.launch {
                    drawerState.close()
                    navController.navigate("home")
                }
            }
        )

        NavigationDrawerItem(
            label = { Text("Lost & Found") },
            selected = false,
            icon = { Icon(Icons.Default.Search, contentDescription = null) },
            onClick = {
                scope.launch {
                    drawerState.close()
                    navController.navigate("lostandfound")
                }
            }
        )

        // For later use...
    }
}
