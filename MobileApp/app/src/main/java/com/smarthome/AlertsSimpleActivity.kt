package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class AlertsSimpleActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts_simple)
        
        // Set up action bar
        supportActionBar?.title = "Alerts"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Setup bottom navigation
        setupBottomNavigation()
    }
    
    private fun setupBottomNavigation() {
        try {
            // Home button
            findViewById<LinearLayout>(R.id.homeNavButton)?.setOnClickListener {
                startActivity(Intent(this, MainSimpleActivity::class.java))
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
            
            // Alerts button (already on alerts page)
            findViewById<LinearLayout>(R.id.alertsNavButton)?.setOnClickListener {
                // Already on alerts page
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
    
    fun openProfileSimpleActivity(view: android.view.View) {
        val intent = Intent(this, ProfileSimpleActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    fun openAlertsActivity(view: android.view.View) {
        // Already on alerts page, no action needed
    }
}
