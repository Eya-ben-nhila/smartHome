package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class EnergySimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_energy_simple)
        
        // Set action bar title
        supportActionBar?.title = "Energy"
        
        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Profile picture click -> go to Profile page
        findViewById<ImageView>(R.id.profilePicture)?.setOnClickListener {
            startActivity(Intent(this, ProfileSimpleActivity::class.java))
        }
        
        // Setup bottom navigation
        setupBottomNavigation()
    }
    
    private fun setupBottomNavigation() {
        try {
            val navigationLayout = findViewById<LinearLayout>(R.id.navigation)
            if (navigationLayout != null) {
                // Home button
                navigationLayout.findViewById<LinearLayout>(R.id.homeNavButton)?.setOnClickListener {
                    val intent = Intent(this, DashboardFinalActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                
                // Security button
                navigationLayout.findViewById<LinearLayout>(R.id.securityNavButton)?.setOnClickListener {
                    startActivity(Intent(this, SecuritySimpleActivity::class.java))
                    finish()
                }
                
                // Energy button (already on energy page)
                navigationLayout.findViewById<LinearLayout>(R.id.energyNavButton)?.setOnClickListener {
                    // Already on energy page
                }
                
                // Activity button
                navigationLayout.findViewById<LinearLayout>(R.id.activityNavButton)?.setOnClickListener {
                    startActivity(Intent(this, ActivitySimpleActivity::class.java))
                    finish()
                }
                
                // Automation button
                navigationLayout.findViewById<LinearLayout>(R.id.automationsNavButton)?.setOnClickListener {
                    startActivity(Intent(this, AutomationSimpleActivity::class.java))
                    finish()
                }
                
                // Alerts button
                navigationLayout.findViewById<LinearLayout>(R.id.alertsNavButton)?.setOnClickListener {
                    startActivity(Intent(this, AlertsSimpleActivity::class.java))
                    finish()
                }
            }
            
        } catch (e: Exception) {
            // Navigation setup error
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
