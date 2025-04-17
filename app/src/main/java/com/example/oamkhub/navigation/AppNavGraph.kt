package com.example.oamkhub.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.oamkhub.data.utils.UserPreferences

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavGraph(navController: NavHostController) {

    val context = LocalContext.current
    val token = remember { UserPreferences(context).getToken() }
    val startDest = if (token.isNullOrEmpty()) "auth" else "marketplace_root"

    NavHost(
        navController = navController,
        startDestination = startDest
    ) {
        authGraph(navController)
        resetGraph(navController)
        generalGraph(navController)
        lostFoundGraph(navController)
        marketplaceGraph(navController)
    }
}
