package com.smarthome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SecuritySimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_security_simple)
        
        // Set action bar title
        supportActionBar?.title = "Security"
        
        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
