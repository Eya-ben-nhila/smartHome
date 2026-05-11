package com.smarthome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import com.smarthome.databinding.ActivityAlertsSimpleBinding

@AndroidEntryPoint
class AlertsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAlertsSimpleBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlertsSimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up navigation
        setupNavigation()
        
        // Load saved alert states
        loadAlertStates()
        
        // Set up filters
        setupFilters()
        
        // Set up Add Rule section
        setupAddRuleSection()
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
            applyAcquittedState(binding.dismissAlert1)
            AppPreferences.setAlertAcquitted("1", true)
            android.widget.Toast.makeText(this, "Alerte acquitée", android.widget.Toast.LENGTH_SHORT).show()
        }
        
        // Second Acquiter button (orange)
        binding.dismissAlert2?.setOnClickListener {
            applyAcquittedState(binding.dismissAlert2)
            AppPreferences.setAlertAcquitted("2", true)
            android.widget.Toast.makeText(this, "Alerte acquitée", android.widget.Toast.LENGTH_SHORT).show()
        }
        
        // Third Acquiter button (blue)
        binding.dismissAlert3?.setOnClickListener {
            applyAcquittedState(binding.dismissAlert3)
            AppPreferences.setAlertAcquitted("3", true)
            android.widget.Toast.makeText(this, "Alerte acquitée", android.widget.Toast.LENGTH_SHORT).show()
        }

        // Fourth Acquiter button (red - gas leak)
        binding.dismissAlert4?.setOnClickListener {
            applyAcquittedState(binding.dismissAlert4)
            AppPreferences.setAlertAcquitted("4", true)
            android.widget.Toast.makeText(this, "Alerte acquitée", android.widget.Toast.LENGTH_SHORT).show()
        }

        // Fifth Acquiter button (orange - low battery)
        binding.dismissAlert5?.setOnClickListener {
            applyAcquittedState(binding.dismissAlert5)
            AppPreferences.setAlertAcquitted("5", true)
            android.widget.Toast.makeText(this, "Alerte acquitée", android.widget.Toast.LENGTH_SHORT).show()
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
