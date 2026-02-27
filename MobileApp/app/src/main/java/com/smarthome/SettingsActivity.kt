package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.CompoundButton
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        
        // Setup switch listeners
        setupSwitchListeners()
        
        // Setup navigation
        setupNavigation()
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
        
        // Two-Factor Switch
        findViewById<Switch>(R.id.twoFactorSwitch)?.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Two-factor auth: ${if (isChecked) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
        }
        
        // Face ID Switch
        findViewById<Switch>(R.id.faceIdSwitch)?.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Face ID: ${if (isChecked) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
        }
        
        // Location Services Switch
        findViewById<Switch>(R.id.locationSwitch)?.setOnCheckedChangeListener { _, isChecked ->
            Toast.makeText(this, "Location services: ${if (isChecked) "enabled" else "disabled"}", Toast.LENGTH_SHORT).show()
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
