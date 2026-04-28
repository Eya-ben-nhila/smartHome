package com.smarthome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {
    
    private lateinit var sharedPreferences: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_simple)
        
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        
        // Setup button click listeners
        setupButtonListeners()
        
        // Setup navigation
        setupNavigation()
    }
    
    private fun setupButtonListeners() {
        // No buttons to setup for now
    }
    
    private fun setupNavigation() {
        // Bottom navigation buttons
        findViewById<LinearLayout>(R.id.homeNavButton)?.setOnClickListener {
            Toast.makeText(this, "Going to Dashboard", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DashboardFinalActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        findViewById<LinearLayout>(R.id.activityNavButton)?.setOnClickListener {
            Toast.makeText(this, "Opening Activity", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ActivityDashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        findViewById<LinearLayout>(R.id.automationsNavButton)?.setOnClickListener {
            Toast.makeText(this, "Opening Automations", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AutomationsActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        findViewById<LinearLayout>(R.id.alertsNavButton)?.setOnClickListener {
            Toast.makeText(this, "Alerts page coming soon", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Alerts page
        }
        
        findViewById<LinearLayout>(R.id.securityNavButton)?.setOnClickListener {
            Toast.makeText(this, "Security page coming soon", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Security page
        }
        
        findViewById<LinearLayout>(R.id.energyNavButton)?.setOnClickListener {
            Toast.makeText(this, "Energy page coming soon", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Energy page
        }
    }
}
