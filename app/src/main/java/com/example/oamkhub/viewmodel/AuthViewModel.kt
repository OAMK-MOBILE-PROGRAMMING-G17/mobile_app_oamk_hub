package com.example.oamkhub.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oamkhub.data.model.login.LoginRequest
import com.example.oamkhub.data.model.register.RegisterRequest
import com.example.oamkhub.data.network.RetrofitInstance
import com.example.oamkhub.data.utils.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel : ViewModel() {
    private val _registerResult = MutableStateFlow<String?>(null)
    val registerResult: StateFlow<String?> = _registerResult

    private val _loginResult = MutableStateFlow<String?>(null)
    val loginResult: StateFlow<String?> = _loginResult

    private val _loginSuccess = MutableStateFlow(false)
    val loginSuccess: StateFlow<Boolean> = _loginSuccess

    private val _otpRequestResult = MutableStateFlow<String?>(null)
    val otpRequestResult: StateFlow<String?> = _otpRequestResult

    private val _otpVerificationResult = MutableStateFlow<Boolean?>(null)
    val otpVerificationResult: StateFlow<Boolean?> = _otpVerificationResult

    private val _resetPasswordResult = MutableStateFlow<String?>(null)
    val resetPasswordResult: StateFlow<String?> = _resetPasswordResult

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

                    if (token != null) {
                        withContext(Dispatchers.IO) {
                            UserPreferences(context).saveToken(token)
                        }
                    }

                    val userId = user?.id?.toString()
                    if (userId != null) {
                        withContext(Dispatchers.IO) {
                            UserPreferences(context).saveUserId(userId)
                        }
                        Log.d("LOGIN_CHECK", "User ID from response: ${user?.id}")

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

    fun requestOtp(email: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.requestOtp(mapOf("email" to email))
                if (response.isSuccessful) {
                    _otpRequestResult.value = response.body()?.get("message")
                } else {
                    _otpRequestResult.value = "Failed to send OTP"
                }
            } catch (e: Exception) {
                _otpRequestResult.value = "Error: ${e.message}"
            }
        }
    }

    fun verifyOtp(email: String, otp: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.verifyOtp(mapOf("email" to email, "otp" to otp))
                _otpVerificationResult.value = response.isSuccessful
            } catch (e: Exception) {
                _otpVerificationResult.value = false
            }
        }
    }

    fun resetPassword(email: String, otp: String, newPassword: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.resetPassword(
                    mapOf("email" to email, "otp" to otp, "newPassword" to newPassword)
                )
                if (response.isSuccessful) {
                    _resetPasswordResult.value = response.body()?.get("message") ?: "Password reset successfully"
                } else {
                    _resetPasswordResult.value = "Password reset is failed"
                }
            } catch (e: Exception) {
                _resetPasswordResult.value = "Error: ${e.message}"
            }
        }
    }

    fun setResetPasswordResult(message: String) {
        _resetPasswordResult.value = message
    }

    fun resetOtpVerificationResult() {
        _otpVerificationResult.value = null
    }

    fun resetLoginResult() {
        _loginResult.value = null
    }
}
