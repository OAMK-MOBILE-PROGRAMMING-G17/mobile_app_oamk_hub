package com.example.oamkhub.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oamkhub.data.model.login.LoginRequest
import com.example.oamkhub.data.model.register.RegisterRequest
import com.example.oamkhub.data.network.RetrofitInstance
import com.example.oamkhub.data.utils.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val _registerResult = MutableStateFlow<String?>(null)
    val registerResult: StateFlow<String?> = _registerResult

    private val _loginResult = MutableStateFlow<String?>(null)
    val loginResult: StateFlow<String?> = _loginResult

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                val request = RegisterRequest(name, email, password)

                Log.d("REGISTER", "Request sent to backend: $request")

                val response = RetrofitInstance.api.registerUser(request)

                Log.d("REGISTER", "Response code: ${response.code()}")

                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("REGISTER", "Response body: $body")
                    _registerResult.value = body?.message ?: "Signup successful"
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("REGISTER", "Signup failed: $errorBody")

                    val friendlyMessage = when {
                        errorBody?.contains("already exists", ignoreCase = true) == true ->
                            "Email address already exists."
                        else -> "Signup failed. Please try again."
                    }

                    _registerResult.value = friendlyMessage
                }

            } catch (e: Exception) {
                Log.e("REGISTER", "Network error: ${e.message}", e)
                _registerResult.value = "Network error: ${e.message}"
            }
        }
    }

    fun login(email: String, password: String, context: Context) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.loginUser(LoginRequest(email, password))

                if (response.isSuccessful) {
                    val user = response.body()?.user
                    val token = response.body()?.token
                    _loginResult.value = "Welcome, ${user?.name}!"
                    _loginSuccess.value = true
                    token?.let {
                        UserPreferences(context).saveToken(it)
                    }
                } else {
                    _loginResult.value = "Login failed: Invalid Credentials"
                    _loginSuccess.value = false
                }

            } catch (e: Exception) {
                _loginResult.value = "Network error: ${e.message}"
            }
        }
    }

    fun resetLoginResult() {
        _loginResult.value = null
    }
}
