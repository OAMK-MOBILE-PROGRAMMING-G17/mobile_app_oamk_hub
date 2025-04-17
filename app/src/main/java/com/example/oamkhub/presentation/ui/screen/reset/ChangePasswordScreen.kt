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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.oamkhub.presentation.ui.theme.PrimaryOrange

@Composable
fun ChangePasswordScreen(navController: NavController, email: String, otp: String, viewModel: AuthViewModel = viewModel()) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val resetPasswordResult by viewModel.resetPasswordResult.collectAsState()

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
        Spacer(modifier = Modifier.height(40.dp))
        Text("Change Password", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = PrimaryOrange)
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = newPassword,
            onValueChange = { newPassword = it },
            label = { Text("New Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (newPassword == confirmPassword) {
                    viewModel.resetPassword(email, otp, newPassword)
                } else {
                    viewModel.setResetPasswordResult("Passwords do not match")
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
        ) {
            Text("Change Password", color = Color.White)
        }
        resetPasswordResult?.let {
            if (it.contains("Password reset")) {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            } else {
                Text(it, color = Color.Red)
            }
        }
    }
}