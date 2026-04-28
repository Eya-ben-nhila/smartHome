package com.smarthome

import android.content.Intent
import android.os.Bundle
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
        
        // Setup bottom navigation
        setupBottomNavigation()
    }
    
    private fun setupBottomNavigation() {
        try {
            // Home button
            findViewById<LinearLayout>(R.id.homeNavButton)?.setOnClickListener {
                startActivity(Intent(this, DashboardFinalActivity::class.java))
                finish()
            }
            
            // Security button
            findViewById<LinearLayout>(R.id.securityNavButton)?.setOnClickListener {
                startActivity(Intent(this, SecuritySimpleActivity::class.java))
                finish()
            }
            
            // Energy button (already on energy page)
            findViewById<LinearLayout>(R.id.energyNavButton)?.setOnClickListener {
                // Already on energy page
            }
            
            // Activity button
            findViewById<LinearLayout>(R.id.activityNavButton)?.setOnClickListener {
                startActivity(Intent(this, ActivitySimpleActivity::class.java))
                finish()
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
    
    // Navigation methods for bottom navigation
    fun openMainActivity(view: android.view.View) {
        val intent = Intent(this, MainSimpleActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    fun openSecurityActivity(view: android.view.View) {
        val intent = Intent(this, SecuritySimpleActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    fun openEnergyActivity(view: android.view.View) {
        // Already on energy page, no action needed
    }
    
    fun openActivityActivity(view: android.view.View) {
        val intent = Intent(this, ActivitySimpleActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    fun openAutomationActivity(view: android.view.View) {
        val intent = Intent(this, AutomationSimpleActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    fun openAlertsActivity(view: android.view.View) {
        val intent = Intent(this, AlertsSimpleActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    fun openProfileSimpleActivity(view: android.view.View) {
        val intent = Intent(this, ProfileSimpleActivity::class.java)
        startActivity(intent)
        finish()
    }
}
