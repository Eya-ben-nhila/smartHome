package com.smarthome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {
    
    private lateinit var sharedPreferences: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        
        // Check if user is already logged in (remember me)
        checkRememberedUser()
        
        // Setup button click listeners
        setupButtonListeners()
    }
    
    private fun setupButtonListeners() {
        // Back button - navigate to main page
        findViewById<ImageView>(R.id.backButton)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        // Login button click
        findViewById<Button>(R.id.loginButton)?.setOnClickListener {
            validateAndLogin()
        }
        
        // Setup signup link click
        findViewById<TextView>(R.id.signupLink)?.setOnClickListener {
            Toast.makeText(this, "Opening Signup", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun checkRememberedUser() {
        val isRemembered = sharedPreferences.getBoolean("rememberMe", false)
        if (isRemembered) {
            // Auto-login if remember me was checked
            Toast.makeText(this, "Welcome back!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DashboardFinalActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    
    private fun validateAndLogin() {
        val emailInput = findViewById<TextInputEditText>(R.id.emailInput)
        val passwordInput = findViewById<TextInputEditText>(R.id.passwordInput)
        val rememberMeCheckbox = findViewById<CheckBox>(R.id.rememberMeCheckbox)
        
        val email = emailInput?.text?.toString()?.trim()
        val password = passwordInput?.text?.toString()?.trim()
        
        // Validation
        if (email.isNullOrEmpty()) {
            emailInput?.error = "Email is required"
            emailInput?.requestFocus()
            Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (password.isNullOrEmpty()) {
            passwordInput?.error = "Password is required"
            passwordInput?.requestFocus()
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Basic email format validation
        if (!isValidEmail(email)) {
            emailInput?.error = "Please enter a valid email"
            emailInput?.requestFocus()
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Save remember me preference
        val editor = sharedPreferences.edit()
        if (rememberMeCheckbox?.isChecked == true) {
            editor.putBoolean("rememberMe", true)
            editor.putString("savedEmail", email)
        } else {
            editor.putBoolean("rememberMe", false)
            editor.remove("savedEmail")
        }
        editor.apply()
        
        // Show success message
        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
        
        // Navigate to main dashboard
        val intent = Intent(this, DashboardFinalActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
