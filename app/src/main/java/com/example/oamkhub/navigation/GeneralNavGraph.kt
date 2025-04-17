package com.example.oamkhub.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.oamkhub.presentation.ui.screen.home.HomeScreen
import com.example.oamkhub.presentation.ui.screen.news.NewsScreen
import com.example.oamkhub.presentation.ui.screen.events.EventsScreen
import com.example.oamkhub.presentation.ui.screen.contacts.ContactScreen

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.generalGraph(navController: NavHostController) {
    navigation(route = "general", startDestination = Routes.General.Home.route) {
        composable(Routes.General.Home.route)    { HomeScreen(navController) }
        composable(Routes.General.News.route)    { NewsScreen(navController) }
        composable(Routes.General.Events.route)  { EventsScreen(navController) }
        composable(Routes.General.Contact.route) { ContactScreen(navController) }
    }
}
