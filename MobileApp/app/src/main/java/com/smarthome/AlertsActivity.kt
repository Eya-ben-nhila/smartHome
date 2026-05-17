package com.smarthome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smarthome.data.repository.SmartHomeRepository
import dagger.hilt.android.AndroidEntryPoint
import com.smarthome.databinding.ActivityAlertsSimpleBinding
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AlertsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAlertsSimpleBinding
    private val repository = SmartHomeRepository()
    private var backendAlerts: List<Map<String, Any>> = emptyList()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlertsSimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up navigation
        setupNavigation()
        
        // Load alerts from backend
        loadAlertsFromBackend()
        
        // Set up filters
        setupFilters()
        
        // Set up Add Rule section
        setupAddRuleSection()
    }
    
    override fun onResume() {
        super.onResume()
        // Reload alerts when activity resumes
        loadAlertsFromBackend()
    }
    
    private fun loadAlertsFromBackend() {
        val token = AppPreferences.getJwtToken()
        if (token == null) {
            android.widget.Toast.makeText(this, "Please login first", android.widget.Toast.LENGTH_SHORT).show()
            return
        }
        
        lifecycleScope.launch {
            val result = repository.getAlerts(token)
            result.onSuccess { alerts ->
                backendAlerts = alerts
                displayAlerts(alerts)
            }.onFailure { error ->
                android.widget.Toast.makeText(this@AlertsActivity, "Failed to load alerts: ${error.message}", android.widget.Toast.LENGTH_SHORT).show()
                android.util.Log.e("AlertsActivity", "Failed to load alerts", error)
                // Fallback to local storage if backend fails
                loadAlertStates()
            }
        }
    }
    
    private fun displayAlerts(alerts: List<Map<String, Any>>) {
        // For now, keep the hardcoded UI but update based on backend data
        // In a full implementation, you would dynamically create alert cards
        android.util.Log.d("AlertsActivity", "Loaded ${alerts.size} alerts from backend")
        
        // Hide all cards initially
        binding.alertCard1?.visibility = android.view.View.GONE
        binding.alertCard2?.visibility = android.view.View.GONE
        binding.alertCard3?.visibility = android.view.View.GONE
        binding.alertCard4?.visibility = android.view.View.GONE
        binding.alertCard5?.visibility = android.view.View.GONE
        
        // Display up to 5 alerts
        alerts.take(5).forEachIndexed { index, alert ->
            val card = when (index) {
                0 -> binding.alertCard1
                1 -> binding.alertCard2
                2 -> binding.alertCard3
                3 -> binding.alertCard4
                4 -> binding.alertCard5
                else -> null
            }
            card?.visibility = android.view.View.VISIBLE
            
            // Update alert details if needed
            val alertId = alert["id"]?.toString() ?: (index + 1).toString()
            val isResolved = alert["isResolved"] as? Boolean ?: false
            
            if (isResolved) {
                val button = when (index) {
                    0 -> binding.dismissAlert1
                    1 -> binding.dismissAlert2
                    2 -> binding.dismissAlert3
                    3 -> binding.dismissAlert4
                    4 -> binding.dismissAlert5
                    else -> null
                }
                applyAcquittedState(button)
            }
        }
        
        // Apply current filter
        updateFilter("all")
    }
    
    private fun setupAddRuleSection() {
        // Initialize Spinner with alert types
        val alertTypes = listOf("Critical", "Warning", "Info")
        val adapter = android.widget.ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            alertTypes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.alertTypeSpinner.adapter = adapter
        
        // Add Rule Button click handler
        binding.addRuleButton.setOnClickListener {
            val type = binding.alertTypeSpinner.selectedItem.toString()
            val condition = binding.conditionEditText.text.toString().trim()
            val threshold = binding.thresholdEditText.text.toString().trim()
            
            if (condition.isNotEmpty() && threshold.isNotEmpty()) {
                // In a real app, this would be saved to a database or server
                android.widget.Toast.makeText(
                    this, 
                    "Règle $type ajoutée avec succès!\nCondition: $condition\nSeuil: $threshold", 
                    android.widget.Toast.LENGTH_LONG
                ).show()
                
                // Clear inputs
                binding.conditionEditText.text.clear()
                binding.thresholdEditText.text.clear()
            } else {
                android.widget.Toast.makeText(
                    this, 
                    "Veuillez remplir tous les champs avant d'ajouter une règle", 
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun loadAlertStates() {
        android.util.Log.d("AlertsActivity", "Loading alert states...")
        if (AppPreferences.isAlertAcquitted("1")) {
            android.util.Log.d("AlertsActivity", "Alert 1 is acquitted")
            applyAcquittedState(binding.dismissAlert1)
        }
        if (AppPreferences.isAlertAcquitted("2")) {
            android.util.Log.d("AlertsActivity", "Alert 2 is acquitted")
            applyAcquittedState(binding.dismissAlert2)
        }
        if (AppPreferences.isAlertAcquitted("3")) {
            android.util.Log.d("AlertsActivity", "Alert 3 is acquitted")
            applyAcquittedState(binding.dismissAlert3)
        }
        if (AppPreferences.isAlertAcquitted("4")) {
            android.util.Log.d("AlertsActivity", "Alert 4 is acquitted")
            applyAcquittedState(binding.dismissAlert4)
        }
        if (AppPreferences.isAlertAcquitted("5")) {
            android.util.Log.d("AlertsActivity", "Alert 5 is acquitted")
            applyAcquittedState(binding.dismissAlert5)
        }
    }

    private fun applyAcquittedState(button: android.widget.TextView?) {
        button?.apply {
            text = "acquitée"
            setTextColor(android.graphics.Color.WHITE)
            setBackgroundResource(R.drawable.rounded_button_green)
        }
    }
    
    private fun setupNavigation() {
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
            val intent = Intent(this, AutomationActivity::class.java)
            startActivity(intent)
        }
        
        binding.navigation.alertsNavButton.setOnClickListener {
            // Already on alerts page
        }
        
        // Profile picture click
        binding.profilePicture?.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }
        
        // Set up Acquiter button click handlers
        setupAcquiterButtons()
    }
    
    private fun setupAcquiterButtons() {
        // First Acquiter button (red)
        binding.dismissAlert1?.setOnClickListener {
            resolveAlertOnBackend(0, binding.dismissAlert1)
        }
        
        // Second Acquiter button (orange)
        binding.dismissAlert2?.setOnClickListener {
            resolveAlertOnBackend(1, binding.dismissAlert2)
        }
        
        // Third Acquiter button (blue)
        binding.dismissAlert3?.setOnClickListener {
            resolveAlertOnBackend(2, binding.dismissAlert3)
        }

        // Fourth Acquiter button (red - gas leak)
        binding.dismissAlert4?.setOnClickListener {
            resolveAlertOnBackend(3, binding.dismissAlert4)
        }

        // Fifth Acquiter button (orange - low battery)
        binding.dismissAlert5?.setOnClickListener {
            resolveAlertOnBackend(4, binding.dismissAlert5)
        }
    }
    
    private fun resolveAlertOnBackend(index: Int, button: android.widget.TextView?) {
        val token = AppPreferences.getJwtToken()
        if (token == null) {
            android.widget.Toast.makeText(this, "Please login first", android.widget.Toast.LENGTH_SHORT).show()
            return
        }
        
        // Get alert ID from backend alerts
        if (index >= backendAlerts.size) {
            // Fallback to local storage if no backend alert
            applyAcquittedState(button)
            AppPreferences.setAlertAcquitted((index + 1).toString(), true)
            android.widget.Toast.makeText(this, "Alerte acquitée", android.widget.Toast.LENGTH_SHORT).show()
            return
        }
        
        val alertId = backendAlerts[index]["id"]?.toString() ?: (index + 1).toString()
        
        lifecycleScope.launch {
            val result = repository.resolveAlert(token, alertId)
            result.onSuccess {
                applyAcquittedState(button)
                android.widget.Toast.makeText(this@AlertsActivity, "Alerte acquitée", android.widget.Toast.LENGTH_SHORT).show()
                loadAlertsFromBackend() // Refresh the list
            }.onFailure { error ->
                android.widget.Toast.makeText(this@AlertsActivity, "Failed to resolve alert: ${error.message}", android.widget.Toast.LENGTH_SHORT).show()
                android.util.Log.e("AlertsActivity", "Failed to resolve alert", error)
            }
        }
    }

    private fun setupFilters() {
        binding.filterAll.setOnClickListener { updateFilter("all") }
        binding.filterCritical.setOnClickListener { updateFilter("critical") }
        binding.filterWarning.setOnClickListener { updateFilter("warning") }
        binding.filterInfo.setOnClickListener { updateFilter("info") }
    }

    private fun updateFilter(filterType: String) {
        // Reset all filters UI
        val filters = listOf(
            binding.filterAll to "all",
            binding.filterCritical to "critical",
            binding.filterWarning to "warning",
            binding.filterInfo to "info"
        )

        for ((view, type) in filters) {
            if (type == filterType) {
                view.setBackgroundResource(R.drawable.oval_filter_active)
                view.setTextColor(android.graphics.Color.WHITE)
            } else {
                view.setBackgroundResource(R.drawable.oval_filter_inactive)
                view.setTextColor(android.graphics.Color.parseColor("#757575"))
            }
        }

        // Show/Hide cards based on filter
        binding.alertCard1?.visibility = if (filterType == "all" || filterType == "critical") android.view.View.VISIBLE else android.view.View.GONE
        binding.alertCard2?.visibility = if (filterType == "all" || filterType == "warning") android.view.View.VISIBLE else android.view.View.GONE
        binding.alertCard3?.visibility = if (filterType == "all" || filterType == "info") android.view.View.VISIBLE else android.view.View.GONE
        binding.alertCard4?.visibility = if (filterType == "all" || filterType == "critical") android.view.View.VISIBLE else android.view.View.GONE
        binding.alertCard5?.visibility = if (filterType == "all" || filterType == "warning") android.view.View.VISIBLE else android.view.View.GONE
    }
}
