package com.example.oamkhub.presentation.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Article
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
            label = { Text("Tweets") },
            selected = false,
            icon = { Icon(Icons.Default.Create, contentDescription = "Main Icon") },
            onClick = {
                scope.launch {
                    drawerState.close()
                    navController.navigate("main")
                }
            }
        )

        NavigationDrawerItem(
            label = { Text("News") },
            selected = false,
            icon = { Icon(Icons.AutoMirrored.Filled.Article, contentDescription = "News icon") },
            onClick = {
                scope.launch {
                    drawerState.close()
                    navController.navigate("news")
                }
            }
        )

        NavigationDrawerItem(
            label = { Text("Events") },
            selected = false,
            icon = { Icon(Icons.Default.Event, contentDescription = "Events icon") },
            onClick = {
                scope.launch {
                    drawerState.close()
                    navController.navigate("events")
                }
            }
        )

        NavigationDrawerItem(
            label = { Text("Lost and Found") },
            selected = false,
            icon = { Icon(Icons.Default.Search, contentDescription = "Search icon") },
            onClick = {
                scope.launch {
                    drawerState.close()
                    navController.navigate("lostfound")
                }
            }
        )

        NavigationDrawerItem(
            label = { Text("MarketPlace") },
            selected = false,
            icon = { Icon(Icons.Default.Sell, contentDescription = "Selling Icon") },
            onClick = {
                scope.launch {
                    drawerState.close()
                    navController.navigate("marketplace")
                }
            }
        )

        NavigationDrawerItem(
            label = { Text("Contact") },
            selected = false,
            icon = { Icon(Icons.Default.ContactPhone, contentDescription = "Contact Icon") },
            onClick = {
                scope.launch {
                    drawerState.close()
                    navController.navigate("contact")
                }
            }
        )
        NavigationDrawerItem(
            label = { Text("About") },
            selected = false,
            icon = { Icon(Icons.Default.Info, contentDescription = "About") },
            onClick = {
                scope.launch {
                    drawerState.close()
                navController.navigate("about")
                }
            }
        )
        NavigationDrawerItem(
            label = { Text("Profile") },
            selected = false,
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile Icon") },
            onClick = {
                scope.launch {
                    drawerState.close()
                    navController.navigate("profile")
                }
            }
        )
       // For later use...
    }
}
