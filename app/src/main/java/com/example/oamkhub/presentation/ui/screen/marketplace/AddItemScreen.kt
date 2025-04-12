package com.example.oamkhub.presentation.ui.screen.marketplace

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.presentation.ui.components.BaseLayout
import com.example.oamkhub.viewmodel.MarketplaceViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddItemScreen(navController: NavController, viewModel: MarketplaceViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var images by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableFloatStateOf(0f) }

    val getImages = rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris: List<Uri> ->
        images = images + uris
    }

    BaseLayout(navController = navController, title = "Add Marketplace Items") { padding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp)
        ) {

            OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
            OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") })
            OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Price") })
            OutlinedTextField(value = endDate, onValueChange = { endDate = it }, label = { Text("End Date") })

            Button(onClick = { getImages.launch(arrayOf("image/*")) }) {
                Text("Pick Images")
            }

            Spacer(modifier = Modifier.height(16.dp))

            images.forEach { uri ->
                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                    AsyncImage(
                        model = uri,
                        contentDescription = "Selected Image",
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = uri.lastPathSegment ?: "Image")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isUploading) {
                LinearProgressIndicator(progress = uploadProgress, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                Button(onClick = {
                    isUploading = false
                    uploadProgress = 0f
                }) {
                    Text("Cancel Upload")
                }
            } else {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            isUploading = true
                            uploadProgress = 0.1f

                            val token = UserPreferences(context).getToken() ?: ""
                            viewModel.createMarketplaceItem(
                                context = context,
                                token = token,
                                title = title,
                                description = description,
                                price = price,
                                endDate = endDate,
                                images = images
                            )


                            repeat(9) {
                                delay(200)
                                uploadProgress += 0.1f
                            }

                            isUploading = false
                            navController.popBackStack()
                        }
                    },
                    enabled = title.isNotEmpty() && description.isNotEmpty() && price.isNotEmpty() && endDate.isNotEmpty() && images.isNotEmpty()
                ) {
                    Text("Create Item")
                }
            }
        }
    }
}
