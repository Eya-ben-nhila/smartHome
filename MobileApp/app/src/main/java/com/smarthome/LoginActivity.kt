package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        
        // Setup login button click
        findViewById<Button>(R.id.loginButton)?.setOnClickListener {
            // Show success message
            Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
            
            // Navigate to main dashboard
            val intent = Intent(this, DashboardFinalActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        // Setup signup link click
        findViewById<TextView>(R.id.signupLink)?.setOnClickListener {
            Toast.makeText(this, "Opening Signup", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}
