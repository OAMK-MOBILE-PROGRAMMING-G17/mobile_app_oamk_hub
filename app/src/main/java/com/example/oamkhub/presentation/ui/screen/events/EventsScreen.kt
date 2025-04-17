package com.example.oamkhub.presentation.ui.screen.events

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.oamkhub.presentation.ui.components.BaseLayout
import com.example.oamkhub.viewmodel.EventViewModel
import java.time.Month
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults

@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EventsScreen(navController: androidx.navigation.NavController) {
    val viewModel = remember { EventViewModel() }
    val eventList by viewModel.events.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadEvents()
    }

    BaseLayout(
        navController = navController,
        title = "Events",
        isRefreshing = false,
        onRefresh = {
            viewModel.loadEvents()
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {

            val selectedMonth by viewModel.selectedMonth.collectAsState()
            var expanded by remember { mutableStateOf(false) }
            val months = Month.entries.toTypedArray()

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    readOnly = true,
                    value = selectedMonth?.name ?: "All Months",
                    onValueChange = {},
                    label = { Text("Filter by Month") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    colors = ExposedDropdownMenuDefaults.textFieldColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("All") },
                        onClick = {
                            viewModel.setMonthFilter(null)
                            expanded = false
                        }
                    )
                    months.forEach { month ->
                        DropdownMenuItem(
                            text = { Text(month.name) },
                            onClick = {
                                viewModel.setMonthFilter(month)
                                expanded = false
                            }
                        )
                    }
                }
            }

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                eventList.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No events found for this month.")
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(eventList) { item ->
                            EventCard(item)
                        }
                    }
                }
            }

        }
    }

}