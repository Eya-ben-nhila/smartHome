package com.smarthome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    
    private lateinit var sharedPreferences: SharedPreferences
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        
        // Setup button listeners
        setupButtonListeners()
        
        // Setup switch listeners
        setupSwitchListeners()
        
        // Setup navigation
        setupNavigation()
    }
    
    private fun setupButtonListeners() {
        // Back button
        findViewById<ImageView>(R.id.backButton)?.setOnClickListener {
            finish() // Go back to previous screen
        }
        
        // Logout button
        findViewById<Button>(R.id.logoutButton)?.setOnClickListener {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
            
            // Clear remember me preferences
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            
            // Navigate back to main page (not dashboard)
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
    
    private fun setupSwitchListeners() {
        // Push Notifications Switch
        findViewById<Switch>(R.id.pushNotificationsSwitch)?.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Push notifications: ${if (isChecked) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
        }
        
        // Email Notifications Switch
        findViewById<Switch>(R.id.emailNotificationsSwitch)?.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Email notifications: ${if (isChecked) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
        }
        
        // Auto-Connect Switch
        findViewById<Switch>(R.id.autoConnectSwitch)?.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Auto-connect: ${if (isChecked) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
        }
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
