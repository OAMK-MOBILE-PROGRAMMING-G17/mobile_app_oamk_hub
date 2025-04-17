package com.example.oamkhub.presentation.ui.screen.reset

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oamkhub.viewmodel.AuthViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.oamkhub.presentation.ui.theme.PrimaryOrange

@Composable
fun OtpScreen(navController: NavController, email: String, viewModel: AuthViewModel = viewModel()) {
    var otp by remember { mutableStateOf("") }
    val otpVerificationResult by viewModel.otpVerificationResult.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.resetOtpVerificationResult()
    }

    BackHandler {
        navController.popBackStack()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        IconButton(onClick = { navController.popBackStack() }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text("Verify OTP", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = PrimaryOrange)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = otp,
            onValueChange = { otp = it },
            label = { Text("OTP") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.verifyOtp(email, otp) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
        ) {
            Text("Verify OTP", color = Color.White)
        }
        otpVerificationResult?.let { result ->
            if (result) {
                navController.navigate("changePassword/$email/$otp")
            } else if (result == false) {
                Text("Invalid OTP. Please try again.", color = Color.Red)
            }
        }
    }
}