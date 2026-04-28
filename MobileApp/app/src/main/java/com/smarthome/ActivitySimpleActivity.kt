package com.smarthome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ActivitySimpleActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity_simple)
        
        // Set up action bar
        supportActionBar?.title = "Activity"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
