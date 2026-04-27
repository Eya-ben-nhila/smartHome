package com.smarthome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class ProfileSimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_simple)
        
        // Set action bar title
        supportActionBar?.title = "Profile"
        
        // Enable back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
