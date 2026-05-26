package com.smarthome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smarthome.data.repository.BackendRequestException
import com.smarthome.data.repository.SmartHomeRepository
import dagger.hilt.android.AndroidEntryPoint
import com.smarthome.databinding.ActivitySigninSimpleBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.io.IOException

@AndroidEntryPoint
class SigninActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySigninSimpleBinding
    private val repository = SmartHomeRepository()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninSimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Pre-fill saved email if remember me was checked
        setupSavedData()
        
        // Set up click listeners
        setupClickListeners()
    }
    
    private fun setupSavedData() {
        val savedEmail = AppPreferences.getUserEmail()
        val savedPassword = AppPreferences.getUserPassword()
        val shouldRemember = AppPreferences.shouldRememberMe()
        
        if (shouldRemember && savedEmail != null && savedPassword != null) {
            binding.emailInput.setText(savedEmail)
            binding.passwordInput.setText(savedPassword)
            binding.rememberMeCheckbox.isChecked = true
        }
    }
    
    private fun setupClickListeners() {
        // Sign in button click
        binding.loginButton?.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString()
            val rememberMe = binding.rememberMeCheckbox.isChecked
            
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Show loading state
                binding.loginButton?.isEnabled = false
                binding.loginButton?.text = getString(R.string.auth_signing_in)
                
                lifecycleScope.launch {
                    try {
                        val result = withTimeoutOrNull(LOGIN_TIMEOUT_MS) {
                            repository.login(email, password)
                        }

                        if (result == null) {
                            android.util.Log.w("SigninActivity", "Backend login timed out; using local mode")
                            completeLocalLogin(email, password, rememberMe)
                            return@launch
                        }

                        result.onSuccess { authResponse ->
                            AppPreferences.setLocalMode(false)
                            authResponse.token?.let { AppPreferences.setJwtToken(it) }
                            authResponse.userId?.let { AppPreferences.setUserId(it) }
                            authResponse.user?.fullName?.let { AppPreferences.setUserName(it) }
                            completeLogin(email, password, rememberMe, authResponse.user?.fullName ?: "", R.string.auth_login_success)
                        }.onFailure { error ->
                            android.util.Log.e("SigninActivity", "Login error", error)
                            if (error is IOException && error !is BackendRequestException) {
                                completeLocalLogin(email, password, rememberMe)
                            } else {
                                android.widget.Toast.makeText(
                                    this@SigninActivity,
                                    getString(R.string.auth_login_failed, error.message ?: ""),
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } finally {
                        if (!isFinishing) {
                            binding.loginButton?.isEnabled = true
                            binding.loginButton?.text = getString(R.string.auth_sign_in)
                        }
                    }
                }
            } else {
                android.widget.Toast.makeText(this, getString(R.string.auth_missing_credentials), android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        
        // Sign up text click
        binding.signupLink?.setOnClickListener {
            // Navigate to sign up activity
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
        
        // Forgot password link click (optional)
        binding.forgotPasswordLink?.setOnClickListener {
            // Handle forgot password (for now, just show a toast)
            android.widget.Toast.makeText(this, getString(R.string.auth_forgot_coming_soon), android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun completeLocalLogin(email: String, password: String, rememberMe: Boolean) {
        AppPreferences.setLocalMode(true)
        completeLogin(email, password, rememberMe, "", R.string.auth_login_offline)
    }

    private fun completeLogin(
        email: String,
        password: String,
        rememberMe: Boolean,
        name: String,
        toastMessageRes: Int
    ) {
        AppPreferences.saveLoginSession(email, password, rememberMe, name)

        if (!rememberMe) {
            AppPreferences.clearCredentials()
        }

        android.widget.Toast.makeText(this@SigninActivity, getString(toastMessageRes), android.widget.Toast.LENGTH_SHORT).show()

        val intent = Intent(this@SigninActivity, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val LOGIN_TIMEOUT_MS = 8_000L
    }
}
