package com.smarthome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SecuritySimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security_simple)
        
        // Set action bar title
        supportActionBar?.title = "Security"
        
        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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
