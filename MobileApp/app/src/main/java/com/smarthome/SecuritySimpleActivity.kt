package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class SecuritySimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security_simple)
        
        // Set action bar title
        supportActionBar?.title = "Security"
        
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
            
            // Security button (already on security page)
            findViewById<LinearLayout>(R.id.securityNavButton)?.setOnClickListener {
                // Already on security page
            }
            
            // Energy button
            findViewById<LinearLayout>(R.id.energyNavButton)?.setOnClickListener {
                startActivity(Intent(this, EnergySimpleActivity::class.java))
                finish()
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
        val intent = Intent(this, DashboardFinalActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    fun openSecurityActivity(view: android.view.View) {
        // Already on security page, no action needed
    }
    
    fun openEnergyActivity(view: android.view.View) {
        val intent = Intent(this, EnergySimpleActivity::class.java)
        startActivity(intent)
        finish()
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
