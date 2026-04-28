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
import android.widget.EditText

class LoginActivity : AppCompatActivity() {
    
    private lateinit var sharedPreferences: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_simple)
        
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        
        // Setup button click listeners
        setupButtonListeners()
    }
    
    private fun setupButtonListeners() {
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
    
    private fun validateAndLogin() {
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
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
