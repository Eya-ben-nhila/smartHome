package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DashboardFinalActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_new)
        
        // Setup button click listeners
        setupButtonListeners()
        
        // Profile picture click -> go to Profile page
        findViewById<ImageView>(R.id.profilePicture)?.setOnClickListener {
            startActivity(Intent(this, ProfileSimpleActivity::class.java))
        }
    }
    
    private fun setupButtonListeners() {
        try {
            // Bottom navigation buttons
            val navigationLayout = findViewById<LinearLayout>(R.id.navigation)
            if (navigationLayout != null) {
                // Home button
                navigationLayout.findViewById<LinearLayout>(R.id.homeNavButton)?.setOnClickListener {
                    // Already on dashboard page, no action needed
                }
                
                // Security button
                navigationLayout.findViewById<LinearLayout>(R.id.securityNavButton)?.setOnClickListener {
                    val intent = Intent(this, SecuritySimpleActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                
                // Energy button
                navigationLayout.findViewById<LinearLayout>(R.id.energyNavButton)?.setOnClickListener {
                    val intent = Intent(this, EnergySimpleActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                
                // Activity button
                navigationLayout.findViewById<LinearLayout>(R.id.activityNavButton)?.setOnClickListener {
                    val intent = Intent(this, ActivitySimpleActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                
                // Automation button
                navigationLayout.findViewById<LinearLayout>(R.id.automationsNavButton)?.setOnClickListener {
                    val intent = Intent(this, AutomationSimpleActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                
                // Alerts button
                navigationLayout.findViewById<LinearLayout>(R.id.alertsNavButton)?.setOnClickListener {
                    val intent = Intent(this, AlertsSimpleActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "ERROR: Navigation layout not found!", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Navigation setup error: ${e.message}", Toast.LENGTH_LONG).show()
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
