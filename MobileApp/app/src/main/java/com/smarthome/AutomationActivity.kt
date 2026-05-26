package com.smarthome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smarthome.data.repository.SmartHomeRepository
import dagger.hilt.android.AndroidEntryPoint
import com.smarthome.databinding.ActivityAutomationSimpleBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AutomationActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAutomationSimpleBinding
    private val repository = SmartHomeRepository()
    private var backendAutomations: List<Map<String, Any>> = emptyList()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAutomationSimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up bottom navigation
        setupBottomNavigation()
        
        // Load automations from backend
        loadAutomationsFromBackend()
    }
    
    override fun onResume() {
        super.onResume()
        // Reload automations when activity resumes
        loadAutomationsFromBackend()
    }
    
    private fun loadAutomationsFromBackend() {
        if (AppPreferences.isLocalMode()) {
            android.util.Log.d("AutomationActivity", "Local mode active; using static automations")
            return
        }

        val token = AppPreferences.getJwtToken()
        if (token == null) {
            android.util.Log.w("AutomationActivity", "No token available; skipping backend automation load")
            return
        }
        
        lifecycleScope.launch {
            val result = repository.getAllAutomations(token)
            result.onSuccess { automations ->
                backendAutomations = automations
                displayAutomations(automations)
            }.onFailure { error ->
                android.widget.Toast.makeText(this@AutomationActivity, "Failed to load automations: ${error.message}", android.widget.Toast.LENGTH_SHORT).show()
                android.util.Log.e("AutomationActivity", "Failed to load automations", error)
            }
        }
    }
    
    private fun displayAutomations(automations: List<Map<String, Any>>) {
        android.util.Log.d("AutomationActivity", "Loaded ${automations.size} automations from backend")
        
        // Log automation details
        automations.forEach { automation ->
            val id = automation["id"]
            val name = automation["automationName"]
            val isActive = automation["isActive"]
            android.util.Log.d("AutomationActivity", "Automation: $name (ID: $id, Active: $isActive)")
        }
        
        // In a full implementation, you would dynamically create automation cards
        // For now, just log the data
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
            val intent = Intent(this, EnergyActivity::class.java)
            startActivity(intent)
        }
        
        binding.navigation.activityNavButton.setOnClickListener {
            val intent = Intent(this, ActivityActivity::class.java)
            startActivity(intent)
        }
        
        binding.navigation.automationsNavButton.setOnClickListener {
            // Already on automation page
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
    
    fun openEnergyActivity(view: android.view.View) {
        val intent = Intent(this, EnergyActivity::class.java)
        startActivity(intent)
    }
    
    fun openSecurityActivity(view: android.view.View) {
        val intent = Intent(this, SecurityActivity::class.java)
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
