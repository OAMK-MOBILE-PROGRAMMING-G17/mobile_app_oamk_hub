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
import androidx.compose.ui.platform.LocalContext
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

    var emailError by remember { mutableStateOf<String?>(null) }

    val loginResult by viewModel.loginResult.collectAsState()
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }

    val context = LocalContext.current

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
                .height(200.dp)
                .width(200.dp)
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
            onValueChange = {
                email = it
                emailError = if(!android.util.Patterns.EMAIL_ADDRESS.matcher(it).matches()){
                    "Invalid email address"
                } else null
            },
            isError = emailError != null,
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            supportingText = {
                if (emailError != null) Text(emailError ?: "", color = MaterialTheme.colorScheme.error)
            }
        )

        Spacer(modifier = Modifier.height(0.dp))

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
                    navController.navigate("resetEmail")
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (email.isNotBlank() && password.isNotBlank() && emailError == null ) {
                    viewModel.login(email, password, context)
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
                onDismissRequest = {
                    showDialog = false
                    if (loginSuccess) {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                    viewModel.resetLoginResult()
                },
                title = { Text(
                    text = "Login Info",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ) },
                text = { Text(dialogMessage) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            viewModel.resetLoginResult()
                            if (loginSuccess) {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "OK",
                            fontSize = 16.sp
                        )
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
