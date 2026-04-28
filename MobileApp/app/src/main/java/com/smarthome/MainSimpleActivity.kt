package com.smarthome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainSimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_simple)
        
        // Set action bar title
        supportActionBar?.title = "Dashboard"
        
        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
    // Navigation methods for bottom navigation
    fun openMainActivity(view: android.view.View) {
        // Already on main page, no action needed
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
