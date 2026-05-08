package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.view.View
import android.os.Handler
import android.os.Looper
import android.widget.EditText
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import org.json.JSONArray
import org.json.JSONObject
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.smarthome.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashboardFinalActivity : AppCompatActivity() {
    
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var additionalDevicesContainer: LinearLayout
    private lateinit var apiService: ApiService
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_new)
        
        sharedPreferences = getSharedPreferences("DevicePrefs", Context.MODE_PRIVATE)
        additionalDevicesContainer = findViewById(R.id.additionalDevicesContainer)
        apiService = ApiService(this)
        
        // Load devices from backend first
        loadDevicesFromBackend()
        
        // Initialize and load static device info
        setupDevices()
        
        // Load dynamic devices
        loadDynamicDevices()
        
        // Setup button click listeners
        setupButtonListeners()
        
        // Profile picture click -> go to Profile page
        findViewById<ImageView>(R.id.profilePicture)?.setOnClickListener {
            startActivity(Intent(this, ProfileSimpleActivity::class.java))
        }
    }
    
    private fun loadDynamicDevices() {
        val dynamicDevicesJson = sharedPreferences.getString("dynamic_devices", "[]")
        try {
            val jsonArray = JSONArray(dynamicDevicesJson)
            for (i in 0 until jsonArray.length()) {
                val deviceJson = jsonArray.getJSONObject(i)
                addDeviceToUi(
                    deviceJson.getString("icon"),
                    deviceJson.getString("name"),
                    deviceJson.getString("location")
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun addDeviceToUi(icon: String, name: String, location: String) {
        val inflater = LayoutInflater.from(this)
        val deviceCard = inflater.inflate(R.layout.item_device_card, additionalDevicesContainer, false)
        
        deviceCard.findViewById<TextView>(R.id.deviceIcon).text = icon
        deviceCard.findViewById<TextView>(R.id.deviceName).text = name
        deviceCard.findViewById<TextView>(R.id.deviceLocation).text = location
        
        // Also add double click to dynamic devices
        deviceCard.setOnDoubleClickListener {
             // For dynamic devices, we could implement editing too, but keeping it simple for now
             Toast.makeText(this, "Editing dynamic devices coming soon!", Toast.LENGTH_SHORT).show()
        }
        
        additionalDevicesContainer.addView(deviceCard)
    }

    private fun showAddDeviceDialog() {
        val context = this
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }
        
        val iconInput = EditText(context).apply {
            hint = "Icon (Emoji)"
            setText("📱")
        }
        
        val nameInput = EditText(context).apply {
            hint = "Device Name"
        }
        
        val locationInput = EditText(context).apply {
            hint = "Location"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 20 }
        }
        
        layout.addView(iconInput)
        layout.addView(nameInput)
        layout.addView(locationInput)
        
        AlertDialog.Builder(context)
            .setTitle("Add New Device")
            .setView(layout)
            .setPositiveButton("Add") { _, _ ->
                val icon = iconInput.text.toString().trim()
                val name = nameInput.text.toString().trim()
                val location = locationInput.text.toString().trim()
                
                if (name.isNotEmpty() && location.isNotEmpty()) {
                    saveAndAddDevice(icon, name, location)
                } else {
                    Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun saveAndAddDevice(icon: String, name: String, location: String) {
        // Add to UI
        addDeviceToUi(icon, name, location)
        
        // Persist
        val dynamicDevicesJson = sharedPreferences.getString("dynamic_devices", "[]")
        try {
            val jsonArray = JSONArray(dynamicDevicesJson)
            val newDevice = JSONObject().apply {
                put("icon", icon)
                put("name", name)
                put("location", location)
            }
            jsonArray.put(newDevice)
            
            sharedPreferences.edit().apply {
                putString("dynamic_devices", jsonArray.toString())
                apply()
            }
            
            Toast.makeText(this, "Device added successfully", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error saving device", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun setupDevices() {
        val devices = listOf(
            DeviceInfo("deviceSmartTV", R.id.deviceSmartTV, R.id.tvName, R.id.tvLocation, "Smart TV", "Living Room"),
            DeviceInfo("deviceSmartLight", R.id.deviceSmartLight, R.id.lightName, R.id.lightLocation, "Smart Light", "Bedroom"),
            DeviceInfo("deviceSmartLock", R.id.deviceSmartLock, R.id.lockName, R.id.lockLocation, "Smart Lock", "Front Door"),
            DeviceInfo("deviceSmartFan", R.id.deviceSmartFan, R.id.fanName, R.id.fanLocation, "Smart Fan", "Living Room")
        )
        
        for (device in devices) {
            val container = findViewById<LinearLayout>(device.containerId)
            val nameView = findViewById<TextView>(device.nameViewId)
            val locationView = findViewById<TextView>(device.locationViewId)
            
            if (container != null && nameView != null && locationView != null) {
                // Load saved info
                val savedName = sharedPreferences.getString("${device.key}_name", device.defaultName)
                val savedLocation = sharedPreferences.getString("${device.key}_location", device.defaultLocation)
                
                nameView.text = savedName
                locationView.text = savedLocation
                
                // Add double click listener
                container.setOnDoubleClickListener {
                    showEditDeviceDialog(device, nameView, locationView)
                }
            }
        }
    }
    
    private fun showEditDeviceDialog(device: DeviceInfo, nameView: TextView, locationView: TextView) {
        val context = this
        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }
        
        val nameInput = EditText(context).apply {
            hint = "Device Name"
            setText(nameView.text)
        }
        
        val locationInput = EditText(context).apply {
            hint = "Location"
            setText(locationView.text)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 20 }
        }
        
        layout.addView(nameInput)
        layout.addView(locationInput)
        
        AlertDialog.Builder(context)
            .setTitle("Edit Device Info")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val newName = nameInput.text.toString().trim()
                val newLocation = locationInput.text.toString().trim()
                
                if (newName.isNotEmpty() && newLocation.isNotEmpty()) {
                    // Update UI
                    nameView.text = newName
                    locationView.text = newLocation
                    
                    // Persist
                    sharedPreferences.edit().apply {
                        putString("${device.key}_name", newName)
                        putString("${device.key}_location", newLocation)
                        apply()
                    }
                    
                    Toast.makeText(context, "Device updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    // Extension for double click
    private fun View.setOnDoubleClickListener(onDoubleClick: () -> Unit) {
        var lastClickTime: Long = 0
        val doubleClickDelay: Long = 300 // ms
        
        this.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime < doubleClickDelay) {
                onDoubleClick()
            }
            lastClickTime = currentTime
        }
    }
    
    data class DeviceInfo(
        val key: String,
        val containerId: Int,
        val nameViewId: Int,
        val locationViewId: Int,
        val defaultName: String,
        val defaultLocation: String
    )
    
    private fun setupButtonListeners() {
        try {
            // Bottom navigation buttons
            val navigationLayout = findViewById<LinearLayout>(R.id.navigation)
            if (navigationLayout != null) {
                // Home button
                navigationLayout.findViewById<LinearLayout>(R.id.homeNavButton)?.setOnClickListener {
                    // Already on dashboard page, no action needed
                }
                
                // Security button
                navigationLayout.findViewById<LinearLayout>(R.id.securityNavButton)?.setOnClickListener {
                    val intent = Intent(this, SecuritySimpleActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                
                // Energy button
                navigationLayout.findViewById<LinearLayout>(R.id.energyNavButton)?.setOnClickListener {
                    val intent = Intent(this, EnergySimpleActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                
                // Activity button
                navigationLayout.findViewById<LinearLayout>(R.id.activityNavButton)?.setOnClickListener {
                    val intent = Intent(this, ActivitySimpleActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                
                // Automation button
                navigationLayout.findViewById<LinearLayout>(R.id.automationsNavButton)?.setOnClickListener {
                    val intent = Intent(this, AutomationSimpleActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                
                // Alerts button
                navigationLayout.findViewById<LinearLayout>(R.id.alertsNavButton)?.setOnClickListener {
                    val intent = Intent(this, AlertsSimpleActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            } else {
                Toast.makeText(this, "ERROR: Navigation layout not found!", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Navigation setup error: ${e.message}", Toast.LENGTH_LONG).show()
        }

        // Add Device button
        findViewById<android.widget.LinearLayout>(R.id.addDeviceButton)?.setOnClickListener {
            showAddDeviceDialog()
        }

        // Alert Trigger Logic
        val alertNotification = findViewById<androidx.cardview.widget.CardView>(R.id.alertNotification)
        val alertTriggerButton = findViewById<ImageView>(R.id.alertTriggerButton)
        val closeAlertButton = findViewById<ImageView>(R.id.closeAlertButton)

        alertTriggerButton?.setOnClickListener {
            alertNotification?.visibility = View.VISIBLE
            
            // Auto-hide after 5 seconds
            Handler(Looper.getMainLooper()).postDelayed({
                alertNotification?.visibility = View.GONE
            }, 5000)
        }

        closeAlertButton?.setOnClickListener {
            alertNotification?.visibility = View.GONE
        }
    }
    
    private fun showProfileSettingsMenu() {
        val options = arrayOf("Profile", "Settings")
        
        AlertDialog.Builder(this)
            .setTitle("Menu")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        Toast.makeText(this, "Opening Profile", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, ProfileSimpleActivity::class.java))
                    }
                    1 -> {
                        Toast.makeText(this, "Opening Settings", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, SettingsActivity::class.java))
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    
    private fun loadDevicesFromBackend() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val result = apiService.getDevices()
                
                result.onSuccess { response ->
                    if (response.optBoolean("success", false)) {
                        val devicesArray = response.optJSONArray("devices")
                        if (devicesArray != null && devicesArray.length() > 0) {
                            // Process and display devices from backend
                            Toast.makeText(this@DashboardFinalActivity, "Loaded ${devicesArray.length()} devices from server", Toast.LENGTH_SHORT).show()
                            
                            // Save devices to local storage for offline use
                            val editor = sharedPreferences.edit()
                            editor.putString("backend_devices", devicesArray.toString())
                            editor.apply()
                        }
                    }
                }
                
                result.onFailure { error ->
                    // Load cached devices if available
                    val cachedDevices = sharedPreferences.getString("backend_devices", null)
                    if (cachedDevices != null) {
                        Toast.makeText(this@DashboardFinalActivity, "Using cached devices", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@DashboardFinalActivity, "Using offline mode", Toast.LENGTH_SHORT).show()
                    }
                }
                
            } catch (e: Exception) {
                Toast.makeText(this@DashboardFinalActivity, "Backend unavailable, using offline mode", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
