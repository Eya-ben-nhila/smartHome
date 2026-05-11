package com.smarthome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import com.smarthome.databinding.ActivityMainSimpleBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainSimpleBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainSimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up click listener for get started button
        binding.getStartedButton.setOnClickListener {
            // Navigate to dashboard activity
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
    }
}
