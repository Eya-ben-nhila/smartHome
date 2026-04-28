package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActivitySimpleActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity_simple)
        
        Toast.makeText(this, "ActivitySimpleActivity onCreate started!", Toast.LENGTH_LONG).show()
        
        // Set up action bar
        supportActionBar?.title = "Activity"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Profile picture click -> go to Profile page
        findViewById<ImageView>(R.id.profilePicture)?.setOnClickListener {
            startActivity(Intent(this, ProfileSimpleActivity::class.java))
        }
        
        // Setup bottom navigation
        setupBottomNavigation()
        
        Toast.makeText(this, "ActivitySimpleActivity setup complete!", Toast.LENGTH_SHORT).show()
    }
    
    private fun setupBottomNavigation() {
        try {
            // Test if navigation layout exists
            val navigationLayout = findViewById<LinearLayout>(R.id.navigation)
            if (navigationLayout == null) {
                Toast.makeText(this, "ERROR: Navigation layout NOT FOUND!", Toast.LENGTH_LONG).show()
                return
            } else {
                Toast.makeText(this, "Navigation layout FOUND!", Toast.LENGTH_SHORT).show()
            }
            
            // Home button
            findViewById<LinearLayout>(R.id.homeNavButton)?.setOnClickListener {
                Toast.makeText(this, "HOME BUTTON CLICKED!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, DashboardFinalActivity::class.java))
                finish()
            }
            
            // Security button
            findViewById<LinearLayout>(R.id.securityNavButton)?.setOnClickListener {
                startActivity(Intent(this, SecuritySimpleActivity::class.java))
                finish()
            }
            
            // Energy button
            findViewById<LinearLayout>(R.id.energyNavButton)?.setOnClickListener {
                startActivity(Intent(this, EnergySimpleActivity::class.java))
                finish()
            }
            
            // Activity button (already on activity page)
            findViewById<LinearLayout>(R.id.activityNavButton)?.setOnClickListener {
                // Already on activity page
            }
            
            // Automation button
            findViewById<LinearLayout>(R.id.automationsNavButton)?.setOnClickListener {
                startActivity(Intent(this, AutomationSimpleActivity::class.java))
                finish()
            }
            
            // Alerts button
            findViewById<LinearLayout>(R.id.alertsNavButton)?.setOnClickListener {
                startActivity(Intent(this, AlertsSimpleActivity::class.java))
                finish()
            }
            
        } catch (e: Exception) {
            // Bottom navigation not available in this layout
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
    // XML navigation methods removed - using programmatic navigation only
    
    fun openProfileSimpleActivity(view: android.view.View) {
        val intent = Intent(this, ProfileSimpleActivity::class.java)
        startActivity(intent)
        finish()
    }
}
