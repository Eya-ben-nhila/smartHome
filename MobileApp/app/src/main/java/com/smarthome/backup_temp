package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smarthome.ui.auth.AuthViewModel
import com.smarthome.ui.auth.LoginState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    
    private val authViewModel: AuthViewModel by viewModels()
    
    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var rememberMeCheckbox: CheckBox
    private lateinit var progressBar: ProgressBar
    private lateinit var errorText: TextView
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_simple)
        
        initializeViews()
        setupObservers()
        setupClickListeners()
        loadSavedPreferences()
    }
    
    private fun initializeViews() {
        emailInput = findViewById(R.id.emailInput)
        passwordInput = findViewById(R.id.passwordInput)
        loginButton = findViewById(R.id.loginButton)
        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox)
        
        // Try to find progress bar or create one if not exists
        progressBar = findViewById(R.id.progressBar) ?: ProgressBar(this).apply {
            id = R.id.progressBar
            visibility = android.view.View.GONE
        }
        
        // Try to find error text or create one
        errorText = findViewById(R.id.errorText) ?: TextView(this).apply {
            id = R.id.errorText
            setTextColor(android.graphics.Color.RED)
            visibility = android.view.View.GONE
        }
    }
    
    private fun setupObservers() {
        // Observe login state
        lifecycleScope.launch {
            authViewModel.loginState.collect { state ->
                when (state) {
                    is LoginState.Loading -> {
                        showLoading(true)
                    }
                    is LoginState.Success -> {
                        showLoading(false)
                        handleLoginSuccess(state.response)
                    }
                    is LoginState.Error -> {
                        showLoading(false)
                        showError(state.message)
                    }
                    LoginState.Idle -> {
                        showLoading(false)
                    }
                }
            }
        }
        
        // Observe loading state
        lifecycleScope.launch {
            authViewModel.isLoading.collect { isLoading ->
                showLoading(isLoading)
            }
        }
        
        // Observe error messages
        lifecycleScope.launch {
            authViewModel.errorMessage.collect { error ->
                if (error != null) {
                    showError(error)
                }
            }
        }
        
        // Check if user is already logged in
        lifecycleScope.launch {
            authViewModel.currentUser.collect { user ->
                if (user != null) {
                    navigateToDashboard()
                }
            }
        }
    }
    
    private fun setupClickListeners() {
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString()
            
            if (validateInput(email, password)) {
                authViewModel.login(email, password)
                
                // Save preferences if remember me is checked
                if (rememberMeCheckbox.isChecked) {
                    savePreferences(email, password)
                }
            }
        }
        
        // Clear error when user starts typing
        emailInput.setOnKeyListener { _, _, _ ->
            authViewModel.clearError()
            false
        }
        
        passwordInput.setOnKeyListener { _, _, _ ->
            authViewModel.clearError()
            false
        }
    }
    
    private fun loadSavedPreferences() {
        val prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        val rememberMe = prefs.getBoolean("rememberMe", false)
        val savedEmail = prefs.getString("savedEmail", "")
        val savedPassword = prefs.getString("savedPassword", "")
        if (rememberMe && !savedEmail.isNullOrEmpty()) {
            emailInput.setText(savedEmail)
            passwordInput.setText(savedPassword)
            rememberMeCheckbox.isChecked = true
        }
    }
    
    private fun validateInput(email: String, password: String): Boolean {
        return when {
            email.isBlank() -> {
                showError("Email cannot be empty")
                false
            }
            !isValidEmail(email) -> {
                showError("Please enter a valid email")
                false
            }
            password.isBlank() -> {
                showError("Password cannot be empty")
                false
            }
            password.length < 6 -> {
                showError("Password must be at least 6 characters")
                false
            }
            else -> true
        }
    }
    
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
    
    private fun showLoading(show: Boolean) {
        progressBar.visibility = if (show) android.view.View.VISIBLE else android.view.View.GONE
        loginButton.isEnabled = !show
    }
    
    private fun showError(message: String) {
        errorText.text = message
        errorText.visibility = android.view.View.VISIBLE
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    
    private fun hideError() {
        errorText.visibility = android.view.View.GONE
    }
    
    private fun handleLoginSuccess(response: com.smarthome.data.model.LoginResponse) {
        hideError()
        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
        navigateToDashboard()
    }
    
    private fun navigateToDashboard() {
        val intent = Intent(this, DashboardFinalActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    
    private fun savePreferences(email: String, password: String) {
        val prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        prefs.edit().apply {
            putBoolean("rememberMe", true)
            putString("savedEmail", email)
            putString("savedPassword", password)
            apply()
        }
    }
    
    private fun clearPreferences() {
        val prefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
}
