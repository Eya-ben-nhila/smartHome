package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class AlertsSimpleActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts_simple)
        
        // Set up action bar
        supportActionBar?.title = "Alerts"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        
        // Profile picture click -> go to Profile page
        findViewById<ImageView>(R.id.profilePicture)?.setOnClickListener {
            startActivity(Intent(this, ProfileSimpleActivity::class.java))
        }
        
        // Setup bottom navigation
        setupBottomNavigation()
        
        // Setup Filters
        setupFilters()
    }
    
    private fun setupFilters() {
        val filterAll = findViewById<TextView>(R.id.filterAll)
        val filterCritical = findViewById<TextView>(R.id.filterCritical)
        val filterWarning = findViewById<TextView>(R.id.filterWarning)
        val filterInfo = findViewById<TextView>(R.id.filterInfo)
        
        val filters = listOf(filterAll, filterCritical, filterWarning, filterInfo)
        
        filterAll?.setOnClickListener { updateFilterState(filterAll, filters, "All") }
        filterCritical?.setOnClickListener { updateFilterState(filterCritical, filters, "Critical") }
        filterWarning?.setOnClickListener { updateFilterState(filterWarning, filters, "Warning") }
        filterInfo?.setOnClickListener { updateFilterState(filterInfo, filters, "Info") }
    }
    
    private fun updateFilterState(selected: TextView, allFilters: List<TextView?>, type: String) {
        // Update UI of filters
        allFilters.forEach { filter ->
            if (filter == selected) {
                filter?.setBackgroundResource(R.drawable.oval_filter_active)
                filter?.setTextColor(android.graphics.Color.WHITE)
            } else {
                filter?.setBackgroundResource(R.drawable.oval_filter_inactive)
                filter?.setTextColor(android.graphics.Color.parseColor("#757575"))
            }
        }
        
        // Filter the cards
        filterAlerts(type)
    }
    
    private fun filterAlerts(type: String) {
        val card1 = findViewById<LinearLayout>(R.id.alertCard1)
        val card2 = findViewById<LinearLayout>(R.id.alertCard2)
        val card3 = findViewById<LinearLayout>(R.id.alertCard3)
        
        when(type) {
            "All" -> {
                card1?.visibility = android.view.View.VISIBLE
                card2?.visibility = android.view.View.VISIBLE
                card3?.visibility = android.view.View.VISIBLE
            }
            "Critical" -> {
                card1?.visibility = android.view.View.VISIBLE
                card2?.visibility = android.view.View.GONE
                card3?.visibility = android.view.View.GONE
            }
            "Warning" -> {
                card1?.visibility = android.view.View.GONE
                card2?.visibility = android.view.View.VISIBLE
                card3?.visibility = android.view.View.GONE
            }
            "Info" -> {
                card1?.visibility = android.view.View.GONE
                card2?.visibility = android.view.View.GONE
                card3?.visibility = android.view.View.VISIBLE
            }
        }
    }

    private fun setupBottomNavigation() {
        try {
            // Home button
            findViewById<LinearLayout>(R.id.homeNavButton)?.setOnClickListener {
                startActivity(Intent(this, DashboardFinalActivity::class.java))
                finish()
            }
            
            // Security button
            findViewById<LinearLayout>(R.id.securityNavButton)?.setOnClickListener {
                startActivity(Intent(this, SecuritySimpleActivity::class.java))
                finish()
            }
            
            // Energy button
            findViewById<LinearLayout>(R.id.energyNavButton)?.setOnClickListener {
                startActivity(Intent(this, EnergySimpleActivity::class.java))
                finish()
            }
            
            // Activity button
            findViewById<LinearLayout>(R.id.activityNavButton)?.setOnClickListener {
                startActivity(Intent(this, ActivitySimpleActivity::class.java))
                finish()
            }
            
            // Automation button
            findViewById<LinearLayout>(R.id.automationsNavButton)?.setOnClickListener {
                startActivity(Intent(this, AutomationSimpleActivity::class.java))
                finish()
            }
            
            // Alerts button (already on alerts page)
            findViewById<LinearLayout>(R.id.alertsNavButton)?.setOnClickListener {
                // Already on alerts page
            }
            
        } catch (e: Exception) {
            // Bottom navigation not available in this layout
        }
        
        // Setup dismiss buttons
        setupDismissButtons()
        
        // Setup add rule functionality
        setupAddRule()
    }

    private fun setupDismissButtons() {
        // Dismiss Alert 1 (Critical)
        findViewById<TextView>(R.id.dismissAlert1)?.setOnClickListener {
            dismissAlert(1, "Security Breach Detected")
        }

        // Dismiss Alert 2 (Warning)
        findViewById<TextView>(R.id.dismissAlert2)?.setOnClickListener {
            dismissAlert(2, "High Energy Consumption")
        }

        // Dismiss Alert 3 (Info)
        findViewById<TextView>(R.id.dismissAlert3)?.setOnClickListener {
            dismissAlert(3, "Motion Detected")
        }
    }

    private fun dismissAlert(alertId: Int, alertName: String) {
        // Show confirmation dialog
        AlertDialog.Builder(this)
            .setTitle("Acquitter l'alerte")
            .setMessage("Êtes-vous sûr de vouloir acquitter l'alerte: $alertName?")
            .setPositiveButton("Acquitter") { dialog, _ ->
                // Update the UI to show 'Résolue'
                updateAlertUI(alertId, alertName)
                
                android.widget.Toast.makeText(this, "Alerte $alertName acquittée", android.widget.Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Annuler") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun updateAlertUI(alertId: Int, alertName: String) {
        val cardId = when(alertId) {
            1 -> R.id.alertCard1
            2 -> R.id.alertCard2
            3 -> R.id.alertCard3
            else -> return
        }
        
        val statusId = when(alertId) {
            1 -> R.id.alertStatus1
            2 -> R.id.alertStatus2
            3 -> R.id.alertStatus3
            else -> return
        }
        
        val buttonId = when(alertId) {
            1 -> R.id.dismissAlert1
            2 -> R.id.dismissAlert2
            3 -> R.id.dismissAlert3
            else -> return
        }
        
        // Update Card Background
        findViewById<LinearLayout>(cardId)?.setBackgroundResource(R.drawable.alert_resolved_background)
        
        // Update Status Text
        val statusView = findViewById<TextView>(statusId)
        statusView?.text = "Résolue"
        statusView?.setTextColor(android.graphics.Color.parseColor("#4CAF50"))
        
        // Update Button
        val button = findViewById<TextView>(buttonId)
        button?.text = "Résolue"
        button?.setBackgroundResource(R.drawable.rounded_button_green)
        button?.isEnabled = false // Disable once resolved
    }

    private fun setupAddRule() {
        // Setup alert type spinner
        val alertTypeSpinner = findViewById<Spinner>(R.id.alertTypeSpinner)
        val alertTypes = arrayOf("Sécurité", "Énergie", "Température", "Mouvement", "Humidité", "Qualité de l'air")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, alertTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        alertTypeSpinner?.adapter = adapter

        // Setup add rule button
        findViewById<TextView>(R.id.addRuleButton)?.setOnClickListener {
            addAlertRule()
        }
    }

    private fun addAlertRule() {
        val alertTypeSpinner = findViewById<Spinner>(R.id.alertTypeSpinner)
        val conditionEditText = findViewById<EditText>(R.id.conditionEditText)
        val thresholdEditText = findViewById<EditText>(R.id.thresholdEditText)

        val alertType = alertTypeSpinner?.selectedItem?.toString() ?: ""
        val condition = conditionEditText?.text?.toString() ?: ""
        val threshold = thresholdEditText?.text?.toString() ?: ""

        // Validate inputs
        if (condition.isEmpty() || threshold.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
            return
        }

        // Show confirmation dialog
        AlertDialog.Builder(this)
            .setTitle("Ajouter une règle d'alerte")
            .setMessage("Type: $alertType\nCondition: $condition\nSeuil: $threshold\n\nConfirmer l'ajout?")
            .setPositiveButton("Ajouter") { dialog, _ ->
                // Add the rule (for now, just show success message)
                Toast.makeText(this, "Règle d'alerte ajoutée avec succès!", Toast.LENGTH_LONG).show()
                
                // Clear the form
                conditionEditText?.text?.clear()
                thresholdEditText?.text?.clear()
                
                // Here you would typically:
                // 1. Save the rule to database
                // 2. Update the alert system
                // 3. Refresh the rules list
                
                dialog.dismiss()
            }
            .setNegativeButton("Annuler") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
