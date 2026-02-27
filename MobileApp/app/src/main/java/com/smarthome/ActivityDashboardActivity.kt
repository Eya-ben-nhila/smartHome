package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ActivityDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actv)

        // Setup button click listeners
        setupButtonListeners()
    }

    private fun setupButtonListeners() {
        // Header settings button
        findViewById<android.widget.ImageView>(R.id.headerSettingsButton)?.setOnClickListener {
            Toast.makeText(this, "Opening Settings", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // Header bell button
        findViewById<android.widget.ImageView>(R.id.headerBellButton)?.setOnClickListener {
            Toast.makeText(this, "Opening Notifications", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Notifications page
        }

        // Bottom navigation buttons
        findViewById<android.widget.LinearLayout>(R.id.homeNavButton)?.setOnClickListener {
            Toast.makeText(this, "Going to Dashboard", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, DashboardFinalActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<android.widget.LinearLayout>(R.id.activityNavButton)?.setOnClickListener {
            Toast.makeText(this, "Activity - Already on this page", Toast.LENGTH_SHORT).show()
        }

        findViewById<android.widget.LinearLayout>(R.id.automationsNavButton)?.setOnClickListener {
            Toast.makeText(this, "Automations page coming soon", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Automations page
        }

        findViewById<android.widget.LinearLayout>(R.id.alertsNavButton)?.setOnClickListener {
            Toast.makeText(this, "Alerts page coming soon", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Alerts page
        }

        findViewById<android.widget.LinearLayout>(R.id.securityNavButton)?.setOnClickListener {
            Toast.makeText(this, "Security page coming soon", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Security page
        }

        findViewById<android.widget.LinearLayout>(R.id.energyNavButton)?.setOnClickListener {
            Toast.makeText(this, "Energy page coming soon", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Energy page
        }
    }
}
