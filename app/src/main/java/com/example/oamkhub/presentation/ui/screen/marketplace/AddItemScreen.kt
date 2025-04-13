package com.example.oamkhub.presentation.ui.screen.marketplace

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import android.app.DatePickerDialog
import androidx.compose.foundation.border
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.graphics.StrokeCap
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.presentation.ui.components.BaseLayout
import com.example.oamkhub.viewmodel.MarketplaceViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ClickableDateField(
    label: String,
    dateText: String,
    onDatePicked: (String) -> Unit,
    placeholder: String = "Tap to pick end date"
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val c = Calendar.getInstance()
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, month)
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val format = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                onDatePicked(format.format(c.time))
                showDialog = false
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.setOnCancelListener { showDialog = false }
        datePicker.show()
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
                .padding(bottom = 4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .border(1.dp, MaterialTheme.colorScheme.outline, MaterialTheme.shapes.small)
                .clickable { showDialog = true }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            if (dateText.isEmpty()) {
                Text(placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant)
            } else {
                Text(dateText, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

fun filterToFloat(input: String): String {
    var dotCount = 0
    return input.filter { char ->
        if (char == '.') {
            if (dotCount == 0) {
                dotCount++
                true
            } else {
                false
            }
        } else {
            char.isDigit()
        }
    }
}


@Composable
fun AddItemScreen(navController: NavController, viewModel: MarketplaceViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var images by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableFloatStateOf(0f) }

    // Pick images
    val getImages =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris: List<Uri> ->
            images = images + uris
        }
    var endDate by remember { mutableStateOf("") }


    // Pick date
    var showDatePicker by remember { mutableStateOf(false) }
    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val c = Calendar.getInstance()
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, month)
                c.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val dateFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
                endDate = dateFormat.format(c.time)

                showDatePicker = false
            },
            2025, 0, 1
        ).show()
    }

    BaseLayout(navController = navController, title = "Add Marketplace Items") { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") }
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description") }
            )

            OutlinedTextField(
                value = price,
                onValueChange = { newValue ->

                    price = filterToFloat(newValue)
                },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                leadingIcon = { Text("€") }
            )


            var endDate by remember { mutableStateOf("") }

            ClickableDateField(
                label = "End Date",
                dateText = endDate,
                onDatePicked = { newDate -> endDate = newDate },
                placeholder = "Tap to pick end date"
            )

            Spacer(modifier = Modifier.height(16.dp))

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
                LinearProgressIndicator(
                progress = { uploadProgress },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                strokeCap = StrokeCap.Round,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        isUploading = false
                        uploadProgress = 0f
                    }
                ) {
                    Text("Cancel Upload")
                }
            } else {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            isUploading = true
                            uploadProgress = 0.1f

                            val token = UserPreferences(context).getToken().orEmpty()
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

                            withContext(Dispatchers.Main) {
                                navController.popBackStack()
                            }
                        }
                    },
                    enabled = title.isNotEmpty() &&
                            description.isNotEmpty() &&
                            price.isNotEmpty() &&
                            endDate.isNotEmpty() &&
                            images.isNotEmpty()
                ) {
                    Text("Create Item")
                }
            }
        }
    }
}