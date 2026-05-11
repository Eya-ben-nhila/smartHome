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
        
        // Load persistent devices
        loadSavedDevices()
    }
    
    private fun loadSavedDevices() {
        binding.additionalDevicesContainer.removeAllViews()
        val devices = AppPreferences.getDevices()
        for (device in devices) {
            addDeviceToUi(device)
        }
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
                    val id = System.currentTimeMillis().toString()
                    val icon = getIconForDevice(name)
                    val device = AppPreferences.Device(id, name, if (location.isEmpty()) "General" else location, icon)
                    
                    AppPreferences.saveDevice(device)
                    addDeviceToUi(device)
                } else {
                    android.widget.Toast.makeText(this, "Please enter a device name", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun getIconForDevice(name: String): String {
        return when {
            name.contains("light", true) -> "💡"
            name.contains("tv", true) -> "📺"
            name.contains("fan", true) -> "🌀"
            name.contains("lock", true) -> "🔒"
            name.contains("cam", true) -> "📷"
            name.contains("speaker", true) -> "🔊"
            else -> "📱"
        }
    }

    private fun addDeviceToUi(device: AppPreferences.Device) {
        val deviceView = layoutInflater.inflate(R.layout.item_device_card, binding.additionalDevicesContainer, false)
        val nameText = deviceView.findViewById<android.widget.TextView>(R.id.deviceName)
        val locationText = deviceView.findViewById<android.widget.TextView>(R.id.deviceLocation)
        val iconText = deviceView.findViewById<android.widget.TextView>(R.id.deviceIcon)
        
        nameText.text = device.name
        locationText.text = device.location
        iconText.text = device.icon
        
        // Double click detection
        var lastClickTime: Long = 0
        deviceView.setOnClickListener {
            val clickTime = System.currentTimeMillis()
            if (clickTime - lastClickTime < 300) {
                // Double click detected
                showEditDeleteDialog(device)
            }
            lastClickTime = clickTime
        }
        
        binding.additionalDevicesContainer.addView(deviceView)
    }

    private fun showEditDeleteDialog(device: AppPreferences.Device) {
        val options = arrayOf("Modify", "Remove Completely")
        android.app.AlertDialog.Builder(this)
            .setTitle(device.name)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showModifyDialog(device)
                    1 -> removeDevice(device)
                }
            }
            .show()
    }

    private fun showModifyDialog(device: AppPreferences.Device) {
        val dialogLayout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }
        
        val nameInput = android.widget.EditText(this).apply {
            hint = "Device Name"
            setText(device.name)
        }
        
        val locationInput = android.widget.EditText(this).apply {
            hint = "Location"
            setText(device.location)
        }

        dialogLayout.addView(nameInput)
        dialogLayout.addView(locationInput)

        android.app.AlertDialog.Builder(this)
            .setTitle("Modify Device")
            .setView(dialogLayout)
            .setPositiveButton("Save") { _, _ ->
                val newName = nameInput.text.toString().trim()
                val newLocation = locationInput.text.toString().trim()
                if (newName.isNotEmpty()) {
                    val updatedDevice = device.copy(name = newName, location = newLocation, icon = getIconForDevice(newName))
                    AppPreferences.saveDevice(updatedDevice)
                    loadSavedDevices() // Refresh UI
                    android.widget.Toast.makeText(this, "Updated!", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun removeDevice(device: AppPreferences.Device) {
        android.app.AlertDialog.Builder(this)
            .setTitle("Remove Device")
            .setMessage("Are you sure you want to remove ${device.name}?")
            .setPositiveButton("Remove") { _, _ ->
                AppPreferences.removeDevice(device.id)
                loadSavedDevices() // Refresh UI
                android.widget.Toast.makeText(this, "Removed", android.widget.Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
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
