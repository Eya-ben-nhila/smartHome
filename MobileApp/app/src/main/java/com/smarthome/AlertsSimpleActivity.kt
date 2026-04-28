package com.smarthome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class AlertsSimpleActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alerts_simple)
        
        // Set up action bar
        supportActionBar?.title = "Alerts"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
