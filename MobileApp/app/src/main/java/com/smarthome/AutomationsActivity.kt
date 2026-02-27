package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class AutomationsActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_automations)
        
        // Setup button click listeners
        setupButtonListeners()
    }
    
    private fun setupButtonListeners() {
        // Header settings button
        findViewById<android.widget.ImageView>(R.id.headerSettingsButton)?.setOnClickListener {
            Toast.makeText(this, "Opening Settings", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
        
        // Header bell button
        findViewById<android.widget.ImageView>(R.id.headerBellButton)?.setOnClickListener {
            Toast.makeText(this, "Opening Notifications", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Notifications page
        }
        
        // Automation switches
        findViewById<Switch>(R.id.morningRoutineSwitch)?.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "enabled" else "disabled"
            Toast.makeText(this, "Morning Routine $status", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<Switch>(R.id.eveningRoutineSwitch)?.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "enabled" else "disabled"
            Toast.makeText(this, "Evening Routine $status", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<Switch>(R.id.awayModeSwitch)?.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "enabled" else "disabled"
            Toast.makeText(this, "Away Mode $status", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<Switch>(R.id.nightModeSwitch)?.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "enabled" else "disabled"
            Toast.makeText(this, "Night Mode $status", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<Switch>(R.id.weekendModeSwitch)?.setOnCheckedChangeListener { _, isChecked ->
            val status = if (isChecked) "enabled" else "disabled"
            Toast.makeText(this, "Weekend Mode $status", Toast.LENGTH_SHORT).show()
        }
        
        // Bottom navigation buttons
        findViewById<android.widget.LinearLayout>(R.id.homeNavButton)?.setOnClickListener {
            Toast.makeText(this, "Going to Home", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DashboardFinalActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        findViewById<android.widget.LinearLayout>(R.id.activityNavButton)?.setOnClickListener {
            Toast.makeText(this, "Opening Activity", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ActivityDashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        findViewById<android.widget.LinearLayout>(R.id.automationsNavButton)?.setOnClickListener {
            Toast.makeText(this, "Automations - Already on this page", Toast.LENGTH_SHORT).show()
        }
        
        findViewById<android.widget.LinearLayout>(R.id.alertsNavButton)?.setOnClickListener {
            Toast.makeText(this, "Alerts page coming soon", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Alerts page
        }
        
        findViewById<android.widget.LinearLayout>(R.id.securityNavButton)?.setOnClickListener {
            Toast.makeText(this, "Security page coming soon", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Security page
        }
        
        findViewById<android.widget.LinearLayout>(R.id.energyNavButton)?.setOnClickListener {
            Toast.makeText(this, "Energy page coming soon", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Energy page
        }
    }
}
