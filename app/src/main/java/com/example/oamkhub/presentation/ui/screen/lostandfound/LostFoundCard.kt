package com.example.oamkhub.presentation.ui.screen.lostandfound

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.oamkhub.data.model.lostandfound.LostProduct

@Composable
fun LostFoundCard(item: LostProduct, onViewComments: () -> Unit, onAddComment: (String) -> Unit) {
    Card(modifier = Modifier.padding(8.dp).fillMaxWidth()) {

        Column(Modifier.padding(16.dp)) {
            Text(text = item.title, fontWeight = FontWeight.Bold)
            Text(text = item.description)
            Text(text = "Location: ${item.location}")
            Text(text = "Lost At: ${item.lostTime}")

            Button(
                onClick = onViewComments,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("View Comments")
            }

            var comment by remember { mutableStateOf("") }

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Add Comment") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    onAddComment(comment)
                    comment = ""
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Submit Comment")
            }

        }
    }
}

