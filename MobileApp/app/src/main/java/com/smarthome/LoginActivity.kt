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
import com.smarthome.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var apiService: ApiService
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_simple)
        
        // Initialize SharedPreferences and API service
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        apiService = ApiService(this)
        
        // Load saved preferences
        loadSavedPreferences()
        
        // Setup button click listeners
        setupButtonListeners()
    }
    
    private fun loadSavedPreferences() {
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val rememberMeCheckbox = findViewById<CheckBox>(R.id.rememberMeCheckbox)
        
        // Check if remember me was checked
        val rememberMe = sharedPreferences.getBoolean("rememberMe", false)
        val savedEmail = sharedPreferences.getString("savedEmail", "")
        val savedPassword = sharedPreferences.getString("savedPassword", "")
        
        if (rememberMe && !savedEmail.isNullOrEmpty()) {
            // Load saved email and password
            emailInput?.setText(savedEmail)
            passwordInput?.setText(savedPassword)
            rememberMeCheckbox?.isChecked = true
        }
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
        
        // Setup remember me checkbox listener
        val rememberMeCheckbox = findViewById<CheckBox>(R.id.rememberMeCheckbox)
        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        
        rememberMeCheckbox?.setOnCheckedChangeListener { _, isChecked ->
            val email = emailInput?.text?.toString()?.trim()
            val password = passwordInput?.text?.toString()?.trim()
            val editor = sharedPreferences.edit()
            
            if (isChecked && !email.isNullOrEmpty()) {
                // Save email and password when checkbox is checked
                editor.putBoolean("rememberMe", true)
                editor.putString("savedEmail", email)
                editor.putString("savedPassword", password)
                Toast.makeText(this, "Email and password will be remembered", Toast.LENGTH_SHORT).show()
            } else if (!isChecked) {
                // Remove saved email and password when checkbox is unchecked
                editor.putBoolean("rememberMe", false)
                editor.remove("savedEmail")
                editor.remove("savedPassword")
                Toast.makeText(this, "Email and password will not be remembered", Toast.LENGTH_SHORT).show()
            }
            editor.apply()
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
            editor.putString("savedPassword", password)
        } else {
            editor.putBoolean("rememberMe", false)
            editor.remove("savedEmail")
            editor.remove("savedPassword")
        }
        editor.apply()
        
        // Show loading message
        Toast.makeText(this, "Logging in...", Toast.LENGTH_SHORT).show()
        
        // Login with backend API
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = apiService.login(email, password)
                
                result.onSuccess { response ->
                    if (response.has("token")) {
                        // Save token and user info
                        val editor = sharedPreferences.edit()
                        editor.putString("authToken", response.getString("token"))
                        editor.putString("userId", response.getString("userId"))
                        editor.putString("userEmail", response.getString("email"))
                        editor.apply()
                        
                        Toast.makeText(this@LoginActivity, "Login successful!", Toast.LENGTH_SHORT).show()
                        
                        // Navigate to main dashboard
                        val intent = Intent(this@LoginActivity, DashboardFinalActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed: ${response.optString("error", "Unknown error")}", Toast.LENGTH_LONG).show()
                    }
                }
                
                result.onFailure { error ->
                    Toast.makeText(this@LoginActivity, "Network error: ${error.message}", Toast.LENGTH_LONG).show()
                }
                
            } catch (e: Exception) {
                // Fallback to local login if backend is not available
                Toast.makeText(this@LoginActivity, "Using offline mode", Toast.LENGTH_SHORT).show()
                
                // Navigate to main dashboard
                val intent = Intent(this@LoginActivity, DashboardFinalActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
    
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
