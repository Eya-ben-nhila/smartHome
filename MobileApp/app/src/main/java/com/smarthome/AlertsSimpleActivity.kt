package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
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
        
        // Setup bottom navigation
        setupBottomNavigation()
    }
    
    private fun setupBottomNavigation() {
        try {
            // Home button
            findViewById<LinearLayout>(R.id.homeNavButton)?.setOnClickListener {
                startActivity(Intent(this, MainSimpleActivity::class.java))
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
                // Dismiss the alert
                android.widget.Toast.makeText(this, "Alerte $alertName acquittée", android.widget.Toast.LENGTH_SHORT).show()
                
                // Here you would typically:
                // 1. Remove the alert from the UI
                // 2. Update the alert status in database
                // 3. Send notification to server
                
                // For now, we'll just show a success message
                dialog.dismiss()
            }
            .setNegativeButton("Annuler") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
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
    
    // Navigation methods for bottom navigation
    fun openMainActivity(view: android.view.View) {
        val intent = Intent(this, MainSimpleActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    fun openSecurityActivity(view: android.view.View) {
        val intent = Intent(this, SecuritySimpleActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    fun openEnergyActivity(view: android.view.View) {
        val intent = Intent(this, EnergySimpleActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    fun openActivityActivity(view: android.view.View) {
        val intent = Intent(this, ActivitySimpleActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    fun openAutomationActivity(view: android.view.View) {
        val intent = Intent(this, AutomationSimpleActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    fun openProfileSimpleActivity(view: android.view.View) {
        val intent = Intent(this, ProfileSimpleActivity::class.java)
        startActivity(intent)
        finish()
    }
    
    fun openAlertsActivity(view: android.view.View) {
        // Already on alerts page, no action needed
    }
}
