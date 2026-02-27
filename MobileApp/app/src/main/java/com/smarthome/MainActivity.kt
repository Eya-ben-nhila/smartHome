package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Setup all click listeners
        setupNavigation()
    }
    
    private fun setupNavigation() {
        // Language button click
        findViewById<ImageButton>(R.id.btnLanguage)?.setOnClickListener {
            // TODO: Show language selection dialog
        }
        
        // Energy button click
        findViewById<LinearLayout>(R.id.energyNavButton)?.setOnClickListener {
            val intent = Intent(this, EnergyActivity::class.java)
            startActivity(intent)
        }
        
        // User profile click
        findViewById<LinearLayout>(R.id.userProfile)?.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        
        // Main login/enter button click
        findViewById<Button>(R.id.loginButton)?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        
        // Swipe hint click (treat as enter button)
        findViewById<LinearLayout>(R.id.bottomHint)?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
