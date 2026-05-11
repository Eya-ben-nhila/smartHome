package com.smarthome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import com.smarthome.databinding.ActivityDashboardNewBinding

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityDashboardNewBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up navigation buttons
        setupNavigation()
    }
    
    private fun setupNavigation() {
        // Profile picture click
        binding.profilePicture.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        
        // Add Device button click
        binding.addDeviceButton?.setOnClickListener {
            showAddDeviceDialog()
        }
        
        // Bottom navigation clicks
        binding.navigation.homeNavButton.setOnClickListener {
            // Already on dashboard (home)
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
            val intent = Intent(this, AutomationActivity::class.java)
            startActivity(intent)
        }
        
        binding.navigation.alertsNavButton.setOnClickListener {
            val intent = Intent(this, AlertsActivity::class.java)
            startActivity(intent)
        }
    }
    
    private fun showAddDeviceDialog() {
        val dialogLayout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }
        
        val nameInput = android.widget.EditText(this).apply {
            hint = "Device Name (e.g. Guest Light)"
        }
        
        val locationInput = android.widget.EditText(this).apply {
            hint = "Location (e.g. Kitchen)"
        }

        dialogLayout.addView(nameInput)
        dialogLayout.addView(locationInput)

        android.app.AlertDialog.Builder(this)
            .setTitle("Add New Device")
            .setView(dialogLayout)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.text.toString().trim()
                val location = locationInput.text.toString().trim()
                if (name.isNotEmpty()) {
                    addNewDeviceToUi(name, if (location.isEmpty()) "General" else location)
                } else {
                    android.widget.Toast.makeText(this, "Please enter a device name", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun addNewDeviceToUi(name: String, location: String) {
        val deviceView = layoutInflater.inflate(R.layout.item_device_card, binding.additionalDevicesContainer, false)
        val nameText = deviceView.findViewById<android.widget.TextView>(R.id.deviceName)
        val locationText = deviceView.findViewById<android.widget.TextView>(R.id.deviceLocation)
        val iconText = deviceView.findViewById<android.widget.TextView>(R.id.deviceIcon)
        
        nameText.text = name
        locationText.text = location
        
        // Pick an icon based on name keywords
        iconText.text = when {
            name.contains("light", true) -> "💡"
            name.contains("tv", true) -> "📺"
            name.contains("fan", true) -> "🌀"
            name.contains("lock", true) -> "🔒"
            name.contains("cam", true) -> "📷"
            name.contains("speaker", true) -> "🔊"
            else -> "📱"
        }
        
        binding.additionalDevicesContainer.addView(deviceView)
        android.widget.Toast.makeText(this, "$name added successfully", android.widget.Toast.LENGTH_SHORT).show()
    }
    
    // Navigation methods for bottom navigation
    fun openMainActivity(view: android.view.View) {
        val intent = Intent(this, MainActivity::class.java)
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

    fun openActivityActivity(view: android.view.View) {
        val intent = Intent(this, ActivityActivity::class.java)
        startActivity(intent)
    }
    
    fun openProfileActivity(view: android.view.View) {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
}
