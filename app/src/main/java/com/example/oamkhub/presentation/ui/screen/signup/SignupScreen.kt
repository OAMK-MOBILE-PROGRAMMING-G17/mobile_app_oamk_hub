package com.example.oamkhub.presentation.ui.screen.signup

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
fun SignupScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordError by remember { mutableStateOf<String?>(null) }
    var emailError by remember { mutableStateOf<String?>(null) }

    val viewModel: AuthViewModel = viewModel()
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("")}
    val registerResult by viewModel.registerResult.collectAsState()

    LaunchedEffect(registerResult) {
        registerResult?.let {
            showDialog = true
            dialogMessage = it
            if (!it.contains("already")) {
                name = ""
                email = ""
                password = ""
                confirmPassword = ""
            }
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
            modifier = Modifier.height(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Create Account",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryOrange
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Create account so you can explore\nall the exciting features of App.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

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
            onValueChange = {
                password = it
                passwordError = if (it != confirmPassword) "Passwords do not match" else null
            },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
                passwordError = if (password != it) "Passwords do not match" else null
            },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            supportingText = {
                if (passwordError != null) Text(passwordError ?: "", color = MaterialTheme.colorScheme.error)
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                if (name.isNotBlank()
                    && email.isNotBlank()
                    && password.isNotBlank()
                    && confirmPassword.isNotBlank()
                    && password == confirmPassword
                    && emailError == null){
                    viewModel.register(name, email, password)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
        ) {
            Text("Sign up", fontSize = 16.sp, color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Already have an account",
            color = PrimaryOrange,
            modifier = Modifier.clickable {
                navController.popBackStack()
            }
        )
    }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Signup Info") },
            text = { (Text(dialogMessage)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        navController.navigate("login"){
                            popUpTo("signup") { inclusive = true }
                        }
                    }
                ) {
                    Text("Go to Login")
                }
            }
        )
    }
}

//@Composable
//fun AuthViewModel() {
//    TODO("Not yet implemented")
//}
