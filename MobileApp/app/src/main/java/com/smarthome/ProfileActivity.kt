package com.smarthome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import com.smarthome.databinding.ActivityProfileSimpleBinding

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityProfileSimpleBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up bottom navigation
        setupBottomNavigation()
    }
    
    private fun setupBottomNavigation() {
        // Bottom navigation click handlers will be set up here
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
    
    fun openEnergyActivity(view: android.view.View) {
        val intent = Intent(this, EnergyActivity::class.java)
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
}
