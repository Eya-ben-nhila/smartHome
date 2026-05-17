package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smarthome.data.repository.SmartHomeRepository
import dagger.hilt.android.AndroidEntryPoint
import com.smarthome.databinding.ActivitySigninSimpleBinding
import kotlinx.coroutines.launch

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
                binding.loginButton?.text = "Signing in..."
                
                // Call backend API
                lifecycleScope.launch {
                    val result = repository.login(email, password)
                    
                    binding.loginButton?.isEnabled = true
                    binding.loginButton?.text = "Sign In"
                    
                    result.onSuccess { authResponse ->
                        // Save JWT token and user ID
                        authResponse.token?.let { AppPreferences.setJwtToken(it) }
                        authResponse.userId?.let { AppPreferences.setUserId(it) }
                        authResponse.user?.fullName?.let { AppPreferences.setUserName(it) }
                        
                        // Save local session for remember me
                        AppPreferences.saveLoginSession(email, password, rememberMe, authResponse.user?.fullName ?: "")
                        
                        if (!rememberMe) {
                            AppPreferences.clearCredentials()
                        }
                        
                        android.widget.Toast.makeText(this@SigninActivity, "Login successful!", android.widget.Toast.LENGTH_SHORT).show()
                        
                        // Navigate to dashboard
                        val intent = Intent(this@SigninActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }.onFailure { error ->
                        android.widget.Toast.makeText(this@SigninActivity, "Login failed: ${error.message}", android.widget.Toast.LENGTH_SHORT).show()
                        android.util.Log.e("SigninActivity", "Login error", error)
                    }
                }
            } else {
                android.widget.Toast.makeText(this, "Please enter email and password", android.widget.Toast.LENGTH_SHORT).show()
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
            android.widget.Toast.makeText(this, "Forgot password feature coming soon!", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
}
