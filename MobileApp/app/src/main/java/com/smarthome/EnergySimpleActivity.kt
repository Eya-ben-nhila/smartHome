package com.smarthome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class EnergySimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_energy_simple)
        
        // Set action bar title
        supportActionBar?.title = "Energy"
        
        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
