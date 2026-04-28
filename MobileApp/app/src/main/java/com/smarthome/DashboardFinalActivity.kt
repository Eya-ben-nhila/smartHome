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
        setContentView(R.layout.activity_dashboard_new)
        
        // Setup button click listeners
        setupButtonListeners()
    }
    
    private fun setupButtonListeners() {
        try {
            // Bottom navigation buttons
            findViewById<android.widget.LinearLayout>(R.id.homeNavButton)?.setOnClickListener {
                val intent = Intent(this, MainSimpleActivity::class.java)
                startActivity(intent)
                finish()
            }
            
            findViewById<android.widget.LinearLayout>(R.id.securityNavButton)?.setOnClickListener {
                val intent = Intent(this, SecuritySimpleActivity::class.java)
                startActivity(intent)
                finish()
            }
            
            findViewById<android.widget.LinearLayout>(R.id.energyNavButton)?.setOnClickListener {
                val intent = Intent(this, EnergySimpleActivity::class.java)
                startActivity(intent)
                finish()
            }
            
            findViewById<android.widget.LinearLayout>(R.id.activityNavButton)?.setOnClickListener {
                val intent = Intent(this, ActivitySimpleActivity::class.java)
                startActivity(intent)
                finish()
            }
            
            findViewById<android.widget.LinearLayout>(R.id.automationsNavButton)?.setOnClickListener {
                val intent = Intent(this, AutomationSimpleActivity::class.java)
                startActivity(intent)
                finish()
            }
            
            findViewById<android.widget.LinearLayout>(R.id.alertsNavButton)?.setOnClickListener {
                val intent = Intent(this, AlertsSimpleActivity::class.java)
                startActivity(intent)
                finish()
            }
        } catch (e: Exception) {
            // Bottom navigation not available in this layout
        }

        // Add Device button
        findViewById<android.widget.LinearLayout>(R.id.addDeviceButton)?.setOnClickListener {
            Toast.makeText(this, "Opening Add Device Screen", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Add Device activity
            // For now, just show a toast message
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
