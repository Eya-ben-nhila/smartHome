package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DashboardNewActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_final)
        
        // Setup button click listeners
        setupButtonListeners()
    }
    
    private fun setupButtonListeners() {
        // Logout button
        findViewById<android.widget.TextView>(R.id.logoutButton)?.setOnClickListener {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
            
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
