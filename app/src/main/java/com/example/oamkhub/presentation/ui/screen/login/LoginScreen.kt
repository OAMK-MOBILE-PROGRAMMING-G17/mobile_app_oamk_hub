package com.example.oamkhub.presentation.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.oamkhub.R
import com.example.oamkhub.presentation.ui.theme.PrimaryOrange
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.oamkhub.viewmodel.AuthViewModel

@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val viewModel: AuthViewModel = viewModel()
    val loginSuccess by viewModel.loginSuccess.collectAsState()

    val loginResult by viewModel.loginResult.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    LaunchedEffect(loginResult) {
        loginResult?.let {
            dialogMessage = it
            showDialog = true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.oamk_logo),
            contentDescription = "OAMK Logo",
            modifier = Modifier
                .height(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Login Here",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryOrange
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Welcome back you’ve been missed!",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Forgot your password?",
                color = PrimaryOrange,
                fontSize = 13.sp,
                modifier = Modifier.clickable {
                    // TODO for later user
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank()) {
                    viewModel.login(email, password)
                } else {
                    dialogMessage = "Please fill in all fields"
                    showDialog = true
                }
                      },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
        ) {
            Text("Sign in", fontSize = 16.sp, color = Color.White)
        }


        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Login Info") },
                text = { Text(dialogMessage) },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        if (loginSuccess) {
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }) {
                        Text("OK")
                    }
                }
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Create new account",
            color = PrimaryOrange,
            modifier = Modifier.clickable {
                navController.navigate("signup")
            }
        )
    }
}
