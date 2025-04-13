package com.example.oamkhub.presentation.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController


@Composable
fun BaseLayout(
    navController: NavController,
    title: String,
    showDrawer: Boolean = true,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    if (showDrawer) {
        ModalNavigationDrawer(
            drawerContent = { DrawerContent(navController, drawerState) },
            drawerState = drawerState
        ) {
            BaseScaffold(navController, drawerState, title, content, bottomBar)
        }
    } else {
        BaseScaffold(navController, drawerState = null, title, content)
    }
}

@Composable
private fun BaseScaffold(
    navController: NavController,
    drawerState: DrawerState?,
    title: String,
    content: @Composable (PaddingValues) -> Unit,
    bottomBar: @Composable () -> Unit = {}
) {
    Scaffold(
        topBar = {
            HubTopBar(title = title, drawerState = drawerState, navController = navController)
        },
        content = content,
        bottomBar = { bottomBar() }
    )
}