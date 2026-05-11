package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import com.smarthome.databinding.ActivitySignupSimpleBinding

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySignupSimpleBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupSimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up click listeners
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        // Sign up button click
        binding.signupButton?.setOnClickListener {
            // Navigate to dashboard after successful signup
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish() // Close sign up activity so user can't go back
        }
        
        // Sign in link click
        binding.signinLink?.setOnClickListener {
            // Navigate back to sign in activity
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
            finish() // Close sign up activity
        }
    }
}
