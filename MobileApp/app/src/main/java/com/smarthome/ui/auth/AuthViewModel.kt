package com.smarthome.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smarthome.data.model.LoginResponse
import com.smarthome.data.model.User
import com.smarthome.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val isLoggedIn = authRepository.isUserLoggedIn()
                if (isLoggedIn) {
                    val user = authRepository.getCurrentUser()
                    user.fold(
                        onSuccess = { _currentUser.value = it },
                        onFailure = { _errorMessage.value = "Failed to load user data" }
                    )
                }
            } catch (e: Exception) {
                _errorMessage.value = "Authentication check failed"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun login(email: String, password: String) {
        if (!validateInput(email, password)) {
            return
        }

        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val result = authRepository.login(email, password)
                result.fold(
                    onSuccess = { response ->
                        _loginState.value = LoginState.Success(response)
                        _currentUser.value = response.user
                    },
                    onFailure = { error ->
                        _loginState.value = LoginState.Error(error.message ?: "Login failed")
                        _errorMessage.value = error.message ?: "Login failed"
                    }
                )
            } catch (e: Exception) {
                _loginState.value = LoginState.Error(e.message ?: "Login failed")
                _errorMessage.value = e.message ?: "Login failed"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = authRepository.logout()
                result.fold(
                    onSuccess = {
                        _currentUser.value = null
                        _loginState.value = LoginState.Idle
                    },
                    onFailure = { error ->
                        _errorMessage.value = error.message ?: "Logout failed"
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Logout failed"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshUser() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = authRepository.getCurrentUser()
                result.fold(
                    onSuccess = { user ->
                        _currentUser.value = user
                    },
                    onFailure = { error ->
                        _errorMessage.value = error.message ?: "Failed to refresh user"
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to refresh user"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    private fun validateInput(email: String, password: String): Boolean {
        return when {
            email.isBlank() -> {
                _errorMessage.value = "Email cannot be empty"
                false
            }
            !isValidEmail(email) -> {
                _errorMessage.value = "Please enter a valid email"
                false
            }
            password.isBlank() -> {
                _errorMessage.value = "Password cannot be empty"
                false
            }
            password.length < 6 -> {
                _errorMessage.value = "Password must be at least 6 characters"
                false
            }
            else -> true
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val response: LoginResponse) : LoginState()
    data class Error(val message: String) : LoginState()
}
