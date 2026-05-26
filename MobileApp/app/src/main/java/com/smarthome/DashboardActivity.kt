package com.smarthome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smarthome.data.api.model.BackendDevice
import com.smarthome.data.api.model.BackendDeviceRequest
import com.smarthome.data.repository.SmartHomeRepository
import com.smarthome.data.api.WebSocketClient
import dagger.hilt.android.AndroidEntryPoint
import com.smarthome.databinding.ActivityDashboardNewBinding
import kotlinx.coroutines.launch
import org.json.JSONObject

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityDashboardNewBinding
    private val repository = SmartHomeRepository()
    private var backendDevices: List<BackendDevice> = emptyList()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardNewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up navigation buttons
        setupNavigation()
        
        loadDevicesFromBackend()
        
        if (!AppPreferences.isLocalMode()) {
            WebSocketClient.connect()
            observeWebSocketMessages()
        }
    }
    
    private fun observeWebSocketMessages() {
        lifecycleScope.launch {
            WebSocketClient.messages.collect { message ->
                runOnUiThread {
                    handleWebSocketMessage(message)
                }
            }
        }
    }
    
    private fun handleWebSocketMessage(message: JSONObject) {
        val type = message.optString("type")
        if (type == "DEVICE_STATUS_UPDATE" || type == "DEVICE_DATA") {
            val data = message.optJSONObject("data")
            val deviceId = message.optString("deviceId", data?.optString("id") ?: "")
            
            if (deviceId.isNotEmpty()) {
                // Find device in our local list and update its status
                val deviceIndex = backendDevices.indexOfFirst { it.id == deviceId }
                if (deviceIndex != -1) {
                    val currentDevice = backendDevices[deviceIndex]
                    var updatedStatus = currentDevice.deviceStatus
                    
                    if (type == "DEVICE_DATA") {
                        // The backend might send string data directly
                        updatedStatus = message.optString("data", updatedStatus)
                    } else if (data != null) {
                        updatedStatus = data.optString("status", currentDevice.deviceStatus)
                    }
                    
                    val updatedDevice = currentDevice.copy(deviceStatus = updatedStatus)
                    
                    // Update list and UI
                    val newList = backendDevices.toMutableList()
                    newList[deviceIndex] = updatedDevice
                    backendDevices = newList
                    displayDevices(backendDevices)
                }
            }
        } else if (type == "DEVICE_ALERT" || type == "SECURITY_ALERT") {
            val alertMsg = message.optString("alert", message.optString("data", "New Alert!"))
            android.widget.Toast.makeText(this, "ALERT: $alertMsg", android.widget.Toast.LENGTH_LONG).show()
        }
    }
    
    override fun onDestroy() {
        super.onDestroy()
        WebSocketClient.disconnect()
    }
    
    override fun onResume() {
        super.onResume()
        // Reload devices when activity resumes
        loadDevicesFromBackend()
    }
    
    private fun loadDevicesFromBackend() {
        if (AppPreferences.isLocalMode()) {
            android.util.Log.d("DashboardActivity", "Local mode active; skipping backend device load")
            return
        }

        val token = AppPreferences.getJwtToken()
        if (token == null) {
            android.util.Log.w("DashboardActivity", "No token available; skipping backend device load")
            return
        }
        
        lifecycleScope.launch {
            val result = repository.getAllDevices(token)
            result.onSuccess { devices ->
                backendDevices = devices
                displayDevices(devices)
            }.onFailure { error ->
                android.widget.Toast.makeText(this@DashboardActivity, "Failed to load devices: ${error.message}", android.widget.Toast.LENGTH_SHORT).show()
                android.util.Log.e("DashboardActivity", "Failed to load devices", error)
            }
        }
    }
    
    private fun displayDevices(devices: List<BackendDevice>) {
        binding.additionalDevicesContainer.removeAllViews()
        for (device in devices) {
            addBackendDeviceToUi(device)
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
            // Already on dashboard (home), reload devices
            loadDevicesFromBackend()
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
        
        val typeInput = android.widget.EditText(this).apply {
            hint = "Type (e.g. LIGHT, THERMOSTAT)"
        }

        dialogLayout.addView(nameInput)
        dialogLayout.addView(locationInput)
        dialogLayout.addView(typeInput)

        android.app.AlertDialog.Builder(this)
            .setTitle("Add New Device")
            .setView(dialogLayout)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.text.toString().trim()
                val location = locationInput.text.toString().trim()
                val type = typeInput.text.toString().trim().uppercase()
                
                if (name.isNotEmpty() && type.isNotEmpty()) {
                    createDeviceOnBackend(name, type, location)
                } else {
                    android.widget.Toast.makeText(this, "Please enter device name and type", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun createDeviceOnBackend(name: String, type: String, location: String) {
        if (AppPreferences.isLocalMode()) {
            AppPreferences.saveDevice(
                AppPreferences.Device(
                    id = System.currentTimeMillis().toString(),
                    name = name,
                    location = if (location.isEmpty()) "General" else location,
                    icon = getIconForDeviceType(type)
                )
            )
            android.widget.Toast.makeText(this, "Device saved locally", android.widget.Toast.LENGTH_SHORT).show()
            return
        }

        val token = AppPreferences.getJwtToken()
        if (token == null) {
            android.util.Log.w("DashboardActivity", "No token available; cannot create device on backend")
            return
        }
        
        lifecycleScope.launch {
            val deviceRequest = BackendDeviceRequest(
                deviceName = name,
                deviceType = type,
                location = if (location.isEmpty()) "General" else location
            )
            
            val result = repository.createDevice(token, deviceRequest)
            result.onSuccess { device ->
                android.widget.Toast.makeText(this@DashboardActivity, "Device created successfully!", android.widget.Toast.LENGTH_SHORT).show()
                loadDevicesFromBackend() // Refresh the list
            }.onFailure { error ->
                android.widget.Toast.makeText(this@DashboardActivity, "Failed to create device: ${error.message}", android.widget.Toast.LENGTH_SHORT).show()
                android.util.Log.e("DashboardActivity", "Failed to create device", error)
            }
        }
    }


    private fun addBackendDeviceToUi(device: BackendDevice) {
        val deviceView = layoutInflater.inflate(R.layout.item_device_card, binding.additionalDevicesContainer, false)
        val nameText = deviceView.findViewById<android.widget.TextView>(R.id.deviceName)
        val locationText = deviceView.findViewById<android.widget.TextView>(R.id.deviceLocation)
        val iconText = deviceView.findViewById<android.widget.TextView>(R.id.deviceIcon)
        
        nameText.text = device.deviceName
        locationText.text = device.location
        iconText.text = getIconForDeviceType(device.deviceType)
        
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
    
    private fun getIconForDeviceType(type: String): String {
        return when (type.uppercase()) {
            "LIGHT" -> "💡"
            "TV" -> "📺"
            "FAN" -> "🌀"
            "DOOR_LOCK" -> "🔒"
            "SECURITY_CAMERA" -> "📷"
            "SPEAKER" -> "🔊"
            "THERMOSTAT" -> "🌡️"
            "MOTION_SENSOR" -> "🚶"
            "SMOKE_DETECTOR" -> "🔥"
            "SMART_PLUG" -> "🔌"
            else -> "📱"
        }
    }

    private fun showEditDeleteDialog(device: BackendDevice) {
        val options = arrayOf("Modify", "Remove Completely")
        android.app.AlertDialog.Builder(this)
            .setTitle(device.deviceName)
            .setItems(options) { _, which ->
                when (which) {
                    0 -> showModifyDialog(device)
                    1 -> removeDevice(device)
                }
            }
            .show()
    }

    private fun showModifyDialog(device: BackendDevice) {
        val dialogLayout = android.widget.LinearLayout(this).apply {
            orientation = android.widget.LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }
        
        val nameInput = android.widget.EditText(this).apply {
            hint = "Device Name"
            setText(device.deviceName)
        }
        
        val locationInput = android.widget.EditText(this).apply {
            hint = "Location"
            setText(device.location)
        }
        
        val typeInput = android.widget.EditText(this).apply {
            hint = "Type"
            setText(device.deviceType)
        }

        dialogLayout.addView(nameInput)
        dialogLayout.addView(locationInput)
        dialogLayout.addView(typeInput)

        android.app.AlertDialog.Builder(this)
            .setTitle("Modify Device")
            .setView(dialogLayout)
            .setPositiveButton("Save") { _, _ ->
                val newName = nameInput.text.toString().trim()
                val newLocation = locationInput.text.toString().trim()
                val newType = typeInput.text.toString().trim().uppercase()
                
                if (newName.isNotEmpty() && newType.isNotEmpty()) {
                    updateDeviceOnBackend(device.id, newName, newType, newLocation)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun updateDeviceOnBackend(deviceId: String, name: String, type: String, location: String) {
        if (AppPreferences.isLocalMode()) {
            android.widget.Toast.makeText(this, "Device changes are local only", android.widget.Toast.LENGTH_SHORT).show()
            return
        }

        val token = AppPreferences.getJwtToken()
        if (token == null) {
            android.util.Log.w("DashboardActivity", "No token available; cannot update device on backend")
            return
        }
        
        lifecycleScope.launch {
            val deviceRequest = BackendDeviceRequest(
                deviceName = name,
                deviceType = type,
                location = if (location.isEmpty()) "General" else location
            )
            
            val result = repository.updateDevice(token, deviceId, deviceRequest)
            result.onSuccess { device ->
                android.widget.Toast.makeText(this@DashboardActivity, "Device updated successfully!", android.widget.Toast.LENGTH_SHORT).show()
                loadDevicesFromBackend() // Refresh the list
            }.onFailure { error ->
                android.widget.Toast.makeText(this@DashboardActivity, "Failed to update device: ${error.message}", android.widget.Toast.LENGTH_SHORT).show()
                android.util.Log.e("DashboardActivity", "Failed to update device", error)
            }
        }
    }

    private fun removeDevice(device: BackendDevice) {
        android.app.AlertDialog.Builder(this)
            .setTitle("Remove Device")
            .setMessage("Are you sure you want to remove ${device.deviceName}?")
            .setPositiveButton("Remove") { _, _ ->
                deleteDeviceOnBackend(device.id)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    
    private fun deleteDeviceOnBackend(deviceId: String) {
        if (AppPreferences.isLocalMode()) {
            android.widget.Toast.makeText(this, "Device changes are local only", android.widget.Toast.LENGTH_SHORT).show()
            return
        }

        val token = AppPreferences.getJwtToken()
        if (token == null) {
            android.util.Log.w("DashboardActivity", "No token available; cannot delete device on backend")
            return
        }
        
        lifecycleScope.launch {
            val result = repository.deleteDevice(token, deviceId)
            result.onSuccess {
                android.widget.Toast.makeText(this@DashboardActivity, "Device removed successfully!", android.widget.Toast.LENGTH_SHORT).show()
                loadDevicesFromBackend() // Refresh the list
            }.onFailure { error ->
                android.widget.Toast.makeText(this@DashboardActivity, "Failed to remove device: ${error.message}", android.widget.Toast.LENGTH_SHORT).show()
                android.util.Log.e("DashboardActivity", "Failed to remove device", error)
            }
        }
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
