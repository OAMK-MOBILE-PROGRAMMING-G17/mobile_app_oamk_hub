package com.example.oamkhub.presentation.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.compose.material.pullrefresh.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BaseLayout(
    navController: NavController,
    title: String,
    showDrawer: Boolean = true,
    isRefreshing: Boolean = false,
    onRefresh: (() -> Unit)? = null,
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val localRefreshing = remember { mutableStateOf(false) }


    val wrappedContent: @Composable (PaddingValues) -> Unit = { paddingValues ->
        if (onRefresh != null) {
            val pullRefreshState = rememberPullRefreshState(
                refreshing = localRefreshing.value,
                onRefresh = {
                    localRefreshing.value = true
                    onRefresh.invoke()

                    scope.launch {
                        delay(100)
                        localRefreshing.value = false
                    }
                }
            )

            Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
                content(paddingValues)
                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(top = 110.dp)
                )
            }
        } else {
            content(paddingValues)
        }
    }

    if (showDrawer) {
        ModalNavigationDrawer(
            drawerContent = { DrawerContent(navController, drawerState) },
            drawerState = drawerState
        ) {
            BaseScaffold(navController, drawerState, title, wrappedContent, bottomBar)
        }
    } else {
        BaseScaffold(navController, drawerState = null, title, wrappedContent, bottomBar)
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