package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import com.smarthome.databinding.ActivitySigninSimpleBinding

@AndroidEntryPoint
class SigninActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySigninSimpleBinding
    
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
            
            // Simple validation (in real app, you'd validate against backend)
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Save login session
                AppPreferences.saveLoginSession(email, password, rememberMe, AppPreferences.getUserName() ?: "")
                
                if (!rememberMe) {
                    // If remember me is not checked, clear specifically email/password 
                    // (though saveLoginSession just updated them, we clear them for next login pre-fill)
                    AppPreferences.clearCredentials()
                }
                
                // Navigate to dashboard after successful login
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish() // Close sign in activity so user can't go back
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
