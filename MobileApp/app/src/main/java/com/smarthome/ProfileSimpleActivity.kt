package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ProfileSimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_simple)
        
        // Set action bar title
        supportActionBar?.title = "Profile"
        
        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Logout button -> go back to welcome page
        findViewById<Button>(R.id.logoutButton)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
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
                
                // Energy button
                navigationLayout.findViewById<LinearLayout>(R.id.energyNavButton)?.setOnClickListener {
                    startActivity(Intent(this, EnergySimpleActivity::class.java))
                    finish()
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
