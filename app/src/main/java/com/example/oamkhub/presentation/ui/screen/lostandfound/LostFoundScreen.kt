package com.example.oamkhub.presentation.ui.screen.lostandfound

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.oamkhub.viewmodel.LostFoundViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.presentation.ui.components.BaseLayout

@Composable
fun LostProductForm(viewModel: LostFoundViewModel, token: String) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var lostTime by remember { mutableStateOf("") }

    Column(Modifier.padding(16.dp)) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
        OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") })
        OutlinedTextField(value = lostTime, onValueChange = { lostTime = it }, label = { Text("Lost Time") })

        Button(onClick = {
            viewModel.submitLostProduct(title, description, location, lostTime, token)
        }) {
            Text("Submit Lost Item")
        }
    }
}

@Composable
fun LostFoundScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: LostFoundViewModel = viewModel()
    val lostItems by viewModel.lostProducts.collectAsState()

    var token by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val savedToken = UserPreferences(context).getToken()
        Log.d("LOST_FOUND", "Retrieved token: $savedToken")

        if (!savedToken.isNullOrEmpty()) {
            token = savedToken
            viewModel.fetchLostItems(savedToken)
        }
    }

    BaseLayout(navController = navController, title = "Lost & Found") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LostProductForm(viewModel = viewModel, token = token)

            LazyColumn {
                items(lostItems) { item ->
                    if (item.id != null) {
                        LostFoundCard(
                            item = item,
                            onViewComments = {
                                viewModel.fetchCommentsForLostItem(item.id!!, token)
                            },
                            onAddComment = { comment ->
                                viewModel.addComment(item.id!!, comment, token)
                            }
                        )
                    } else {
                        Log.e("LOST_FOUND", "Lost item has null ID: $item")
                    }
                }
            }

        }
    }
}
