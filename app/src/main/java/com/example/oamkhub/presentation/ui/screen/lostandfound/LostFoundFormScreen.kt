package com.example.oamkhub.presentation.ui.screen.lostandfound

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.presentation.ui.components.BaseLayout
import com.example.oamkhub.viewmodel.LostFoundViewModel

@Composable
fun LostFoundFormScreen(navController: NavController, viewModel: LostFoundViewModel) {
    val context = LocalContext.current
    var token by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val savedToken = UserPreferences(context).getToken()
        if (!savedToken.isNullOrEmpty()) {
            token = savedToken
        }
    }

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var lostTime by remember { mutableStateOf("") }

    BaseLayout(navController = navController, title = "Report Lost Item") { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
            OutlinedTextField(value = location, onValueChange = { location = it }, label = { Text("Location") })
            OutlinedTextField(value = lostTime, onValueChange = { lostTime = it }, label = { Text("Lost Time") })

            Button(onClick = {
                viewModel.submitLostProduct(title, description, location, lostTime, token)
                navController.popBackStack()
            }) {
                Text("Submit Lost Item")
            }
        }
    }
}
