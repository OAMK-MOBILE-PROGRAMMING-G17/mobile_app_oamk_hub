package com.example.oamkhub.presentation.ui.screen.about

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oamkhub.presentation.ui.components.BaseLayout

@Composable
fun AboutScreen(navController: NavController) {
    val contributors = listOf(
        "Anil Shah -  Frontend Developer, Designer",
        "Bibek Tandon - Backend Developer",
        "Pabitra Kunwar - Project Manager",
    )

    BaseLayout(
        navController = navController,
        title = "About",
        showDrawer = true
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // App Information Section
            Text(
                text = "About OAMK Hub",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text =" OAMK Hub is a student-focused mobile application designed to centralize all essential services, announcements, and community tools for Oulu University of Applied Sciences (OAMK). Stay informed, connected, and productive — all in one place.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Contributors Section
            Text(
                text = "Contributors",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(contributors) { contributor ->
                    ContributorCard(contributor)
                }
            }
        }
    }
}

@Composable
fun ContributorCard(name: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(16.dp)
        )
    }
}