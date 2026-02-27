package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class AlertsActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts)
        
        // Setup navigation
        setupNavigation()
    }
    
    private fun setupNavigation() {
        // Bottom navigation buttons
        findViewById<LinearLayout>(R.id.homeNavButton)?.setOnClickListener {
            val intent = Intent(this, DashboardFinalActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        findViewById<LinearLayout>(R.id.activityNavButton)?.setOnClickListener {
            val intent = Intent(this, ActivityDashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        findViewById<LinearLayout>(R.id.automationsNavButton)?.setOnClickListener {
            val intent = Intent(this, AutomationsActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        findViewById<LinearLayout>(R.id.alertsNavButton)?.setOnClickListener {
            // Already on alerts page - do nothing
        }
        
        findViewById<LinearLayout>(R.id.securityNavButton)?.setOnClickListener {
            val intent = Intent(this, SecurityActivity::class.java)
            startActivity(intent)
            finish()
        }
        
        findViewById<LinearLayout>(R.id.energyNavButton)?.setOnClickListener {
            val intent = Intent(this, EnergyActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
