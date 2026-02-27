package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DashboardFinalActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_final)
        
        // Setup button click listeners
        setupButtonListeners()
    }
    
    private fun setupButtonListeners() {
        // Menu icon (hamburger menu)
        findViewById<ImageView>(R.id.menuIcon)?.setOnClickListener {
            showProfileSettingsMenu()
        }
        
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
        
        // Bottom navigation buttons
        findViewById<android.widget.LinearLayout>(R.id.homeNavButton)?.setOnClickListener {
            Toast.makeText(this, "Going to Home", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
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
            Toast.makeText(this, "Opening Automations", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AutomationsActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        findViewById<android.widget.LinearLayout>(R.id.alertsNavButton)?.setOnClickListener {
            Toast.makeText(this, "Opening Alerts", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AlertsActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        findViewById<android.widget.LinearLayout>(R.id.securityNavButton)?.setOnClickListener {
            Toast.makeText(this, "Opening Security", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SecurityActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        findViewById<android.widget.LinearLayout>(R.id.energyNavButton)?.setOnClickListener {
            Toast.makeText(this, "Opening Energy", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, EnergyActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    
    private fun showProfileSettingsMenu() {
        val options = arrayOf("Profile", "Settings")
        
        AlertDialog.Builder(this)
            .setTitle("Menu")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        Toast.makeText(this, "Opening Profile", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, ProfileActivity::class.java))
                    }
                    1 -> {
                        Toast.makeText(this, "Opening Settings", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, SettingsActivity::class.java))
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
