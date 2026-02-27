package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SignupActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        
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
        
        // Signup button click
        findViewById<Button>(R.id.signupButton)?.setOnClickListener {
            // Show success message
            Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT).show()
            
            // Navigate to login page
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
