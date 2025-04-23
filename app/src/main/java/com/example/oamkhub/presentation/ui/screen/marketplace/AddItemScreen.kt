package com.example.oamkhub.presentation.ui.screen.marketplace

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.LinearProgressIndicator
import com.example.oamkhub.data.utils.UserPreferences
import com.example.oamkhub.presentation.ui.components.BaseLayout
import com.example.oamkhub.viewmodel.MarketplaceViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.result.launch
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File
import android.graphics.Bitmap
import android.app.DatePickerDialog
import java.util.Calendar
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.content.Context

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
    var endDate by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var gpsLocation by remember { mutableStateOf<Pair<Double, Double>?>(null) }

    val fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    val calendar = Calendar.getInstance()

    // DatePickerDialog to select the end date
    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            endDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )


    // Permission launcher
    val requestPermissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            fetchCurrentLocation(context, fusedLocationClient) { location ->
                gpsLocation = location
            }
        } else {
            android.util.Log.d("AddItemScreen", "Location permissions denied")
        }
    }

    fun checkAndRequestPermissions() {
        val locationManager = context.getSystemService(LocationManager::class.java)
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions
            requestPermissionsLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else if (!isGpsEnabled) {
            // Prompt user to enable GPS
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        } else {
            // Fetch current location
            fetchCurrentLocation(context, fusedLocationClient) { location ->
                gpsLocation = location
            }
        }
    }


    val getImages =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenMultipleDocuments()) { uris: List<Uri> ->
            images = images + uris
        }

    val captureImageFromCamera =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                // Convert Bitmap to a temporary file
                val tempFile = File.createTempFile("captured_image", ".jpg", context.cacheDir).apply {
                    outputStream().use { stream ->
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
                    }
                }
                // Get URI for the temporary file
                val uri = Uri.fromFile(tempFile)
                images = images + uri // Add URI to the list of images
            }
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
                onValueChange = { newValue -> price = filterToFloat(newValue) },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
                leadingIcon = { Text("€") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Button to open DatePickerDialog
            Button(onClick = { datePickerDialog.show() }) {
                Text(if (endDate.isEmpty()) "Select End Date" else "End Date: $endDate")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Address") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { checkAndRequestPermissions() }) {
                Text("Get Current Location")
            }

            gpsLocation?.let {
                Text(
                    text = "Latitude: ${it.first}, Longitude: ${it.second}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { getImages.launch(arrayOf("image/*")) }) {
                Text("Pick Images from Gallery")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { captureImageFromCamera.launch() }) {
                Text("Capture Image from Camera")
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
                    progress = uploadProgress,
                    modifier = Modifier.fillMaxWidth()
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
                                address = address,
                                gpsLocation = gpsLocation,
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
                            address.isNotEmpty() &&
                            gpsLocation != null &&
                            images.isNotEmpty()
                ) {
                    Text("Create Item")
                }
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


fun fetchCurrentLocation(
    context: Context,
    fusedLocationClient: FusedLocationProviderClient,
    onLocationFetched: (Pair<Double, Double>?) -> Unit
) {
    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
        ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    ) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                onLocationFetched(Pair(location.latitude, location.longitude))
            } else {
                onLocationFetched(null)
            }
        }.addOnFailureListener {
            onLocationFetched(null)
        }
    } else {
        onLocationFetched(null)
    }
}