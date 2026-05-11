package com.smarthome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import com.smarthome.databinding.ActivityEnergySimpleBinding

@AndroidEntryPoint
class EnergyActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityEnergySimpleBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnergySimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up bottom navigation
        setupBottomNavigation()
    }
    
    private fun setupBottomNavigation() {
        // Bottom navigation clicks
        binding.navigation.homeNavButton.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
        
        binding.navigation.securityNavButton.setOnClickListener {
            val intent = Intent(this, SecurityActivity::class.java)
            startActivity(intent)
        }
        
        binding.navigation.energyNavButton.setOnClickListener {
            // Already on energy page
        }
        
        binding.navigation.activityNavButton.setOnClickListener {
            val intent = Intent(this, ActivityActivity::class.java)
            startActivity(intent)
        }
        
        binding.navigation.automationsNavButton.setOnClickListener {
            val intent = Intent(this, AutomationActivity::class.java)
            startActivity(intent)
        }
        
        binding.navigation.alertsNavButton.setOnClickListener {
            val intent = Intent(this, AlertsActivity::class.java)
            startActivity(intent)
        }
        
        // Profile picture click (if exists in layout)
        binding.profilePicture?.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
    }
    
    // Navigation methods for bottom navigation
    fun openMainActivity(view: android.view.View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    
    fun openDashboardActivity(view: android.view.View) {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }
    
    fun openSecurityActivity(view: android.view.View) {
        val intent = Intent(this, SecurityActivity::class.java)
        startActivity(intent)
    }
    
    fun openAutomationActivity(view: android.view.View) {
        val intent = Intent(this, AutomationActivity::class.java)
        startActivity(intent)
    }

    fun openActivityActivity(view: android.view.View) {
        val intent = Intent(this, ActivityActivity::class.java)
        startActivity(intent)
    }
    
    fun openProfileActivity(view: android.view.View) {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
}
