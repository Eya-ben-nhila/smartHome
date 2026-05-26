package com.smarthome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smarthome.data.repository.SmartHomeRepository
import dagger.hilt.android.AndroidEntryPoint
import com.smarthome.databinding.ActivityEnergySimpleBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EnergyActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityEnergySimpleBinding
    private val repository = SmartHomeRepository()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnergySimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up bottom navigation
        setupBottomNavigation()
        
        // Load energy data from backend
        loadEnergyDashboard()
    }
    
    override fun onResume() {
        super.onResume()
        // Reload energy data when activity resumes
        loadEnergyDashboard()
    }
    
    private fun loadEnergyDashboard() {
        if (AppPreferences.isLocalMode()) {
            android.util.Log.d("EnergyActivity", "Local mode active; using static energy dashboard")
            return
        }

        val token = AppPreferences.getJwtToken()
        if (token == null) {
            android.util.Log.w("EnergyActivity", "No token available; skipping backend energy load")
            return
        }
        
        lifecycleScope.launch {
            val result = repository.getEnergyDashboard(token)
            result.onSuccess { dashboard ->
                displayEnergyData(dashboard)
            }.onFailure { error ->
                android.widget.Toast.makeText(this@EnergyActivity, "Failed to load energy data: ${error.message}", android.widget.Toast.LENGTH_SHORT).show()
                android.util.Log.e("EnergyActivity", "Failed to load energy data", error)
            }
        }
    }
    
    private fun displayEnergyData(dashboard: Map<String, Any>) {
        android.util.Log.d("EnergyActivity", "Energy dashboard data: $dashboard")
        
        // Extract values from the dashboard map
        val totalConsumption = dashboard["totalConsumption"] as? Double ?: 0.0
        val totalCost = dashboard["totalCost"] as? Double ?: 0.0
        val averagePower = dashboard["averagePower"] as? Double ?: 0.0
        
        // Update UI 
        binding.dailyAvgValue?.text = String.format("%.1f", averagePower)
        binding.monthlyTotalValue?.text = String.format("%.1f", totalConsumption)
        binding.currentCostValue?.text = String.format("$%.2f", totalCost / 30) // estimate daily
        binding.monthlyCostValue?.text = String.format("$%.2f", totalCost)
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
