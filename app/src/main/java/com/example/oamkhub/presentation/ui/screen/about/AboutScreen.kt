package com.example.oamkhub.presentation.ui.screen.about

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oamkhub.presentation.ui.components.BaseLayout
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable

@Composable
fun AboutScreen(navController: NavController) {
    val contributors = listOf(
        "Anil Shah - Full Stack Developer",
        "Bibek Tandon - Full Stack Developer",
        "Pabitra Kunwar - Frontend Developer,Project Management"
    )

    val libraries = listOf(
        "Jetpack Compose - UI Toolkit",
        "Kotlin - Programming Language",
        "NodeJs - Backend Services",
        "Material3 - Design System"
    )

    BaseLayout(
        navController = navController,
        title = "About",
        showDrawer = true
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // App Name and Description
            item {
                Text(
                    text = "📱 OAMK Hub",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "OAMK Hub is a student-focused mobile application designed to centralize all essential services, announcements, and community tools for Oulu University of Applied Sciences (OAMK). Stay informed, connected, and productive — all in one place.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Features Section
            item {
                ExpandableSection(title = "🌟 Key Features") {
                    Text("🧑‍🎓 Student Login – Secure authentication using your OAMK credentials")
                    Text("📢 Announcements – Stay updated with university-wide news and alerts")
                    Text("📆 Events Calendar – View and RSVP to upcoming campus events")
                    Text("🛍️ Marketplace – Buy, sell, or trade items with fellow students")
                    Text("🔍 Lost & Found – Report or search for lost and found items")
                    Text("💬 Discussion Forum – Ask questions, join discussions, and get help")
                    Text("📚 Resource Center – Access documents, guides, and links")
                    Text("📞 Emergency Contacts – Quick access to university and local emergency numbers")
                }
            }

            // Contributors Section
            item {
                ExpandableSection(title = "👥 Contributors") {
                    contributors.forEach { contributor ->
                        ContributorCard(contributor)
                    }
                }
            }

            // Technical Info Section
            item {
                ExpandableSection(title = "⚙️ Technical Info") {
                    libraries.forEach { library ->
                        Text("• $library", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }

            // Privacy Policy Section
            item {
                ExpandableSection(title = "🔐 Privacy Policy") {
                    Text(
                        text = "We take your data seriously. All communication is encrypted, and we only store what's necessary to provide you with the best service.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Contact Section
            item {
                ExpandableSection(title = "📬 Contact & Support") {
                    Text("📧 Email: support@oamkhub.com")
                    Text("🌐 Website: www.oamkhub.com")
                    Text("🐙 GitHub: github.com/oamkhub")
                }
            }

            // License Section
            item {
                ExpandableSection(title = "📝 License") {
                    Text("© 2025 OAMK Hub Team. All rights reserved.")
                    Text("This app is licensed under the MIT License.")
                }
            }
        }
    }
}

@Composable
fun ExpandableSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                content()
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