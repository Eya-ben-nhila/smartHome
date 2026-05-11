package com.smarthome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import com.smarthome.databinding.ActivityProfileSimpleBinding

@AndroidEntryPoint
class ProfileActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityProfileSimpleBinding
    private var isEditing = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileSimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up bottom navigation
        setupBottomNavigation()
        
        // Load user data
        loadUserData()
        
        // Set up click listeners
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        // Logout button
        binding.logoutButton.setOnClickListener {
            logout()
        }
        
        // Edit profile button
        binding.editProfileButton.setOnClickListener {
            if (isEditing) {
                saveProfileChanges()
            } else {
                toggleEditing(true)
            }
        }
        
        // Change photo text
        binding.changePhotoText.setOnClickListener {
            android.widget.Toast.makeText(this, "Change photo feature coming soon!", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun loadUserData() {
        val userEmail = AppPreferences.getUserEmail()
        val userName = AppPreferences.getUserName()
        
        binding.profileEmailInput.setText(userEmail ?: "")
        binding.profileNameInput.setText(userName ?: "User Name")
        
        // Ensure editing is disabled initially
        toggleEditing(false)
        
        android.util.Log.d("ProfileActivity", "Loaded email: $userEmail, name: $userName")
    }
    
    private fun toggleEditing(enable: Boolean) {
        isEditing = enable
        binding.profileNameInput.isEnabled = enable
        binding.profileEmailInput.isEnabled = enable
        binding.profileNameInput.isFocusable = enable
        binding.profileEmailInput.isFocusable = enable
        binding.profileNameInput.isFocusableInTouchMode = enable
        binding.profileEmailInput.isFocusableInTouchMode = enable
        
        if (enable) {
            binding.profileNameInput.requestFocus()
            binding.editProfileButton.text = "Save Changes"
            // Set background to a different color to indicate save action
            binding.editProfileButton.setBackgroundColor(android.graphics.Color.parseColor("#4CAF50")) // Green
        } else {
            binding.editProfileButton.text = "Edit Profile"
            binding.editProfileButton.setBackgroundColor(android.graphics.Color.parseColor("#2196F3")) // Blue
        }
    }
    
    private fun saveProfileChanges() {
        val newName = binding.profileNameInput.text.toString().trim()
        val newEmail = binding.profileEmailInput.text.toString().trim()
        
        if (newName.isNotEmpty() && newEmail.isNotEmpty()) {
            // Save to AppPreferences
            AppPreferences.setUserName(newName)
            
            // If email changed, we should probably update it in the session too
            val currentPassword = AppPreferences.getUserPassword()
            AppPreferences.saveLoginSession(newEmail, currentPassword ?: "", AppPreferences.shouldRememberMe(), newName)
            
            toggleEditing(false)
            android.widget.Toast.makeText(this, "Profile updated successfully!", android.widget.Toast.LENGTH_SHORT).show()
        } else {
            android.widget.Toast.makeText(this, "Please fill in all fields", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun logout() {
        AppPreferences.logout()
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    
    private fun setupBottomNavigation() {
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
            val intent = Intent(this, AlertsActivity::class.java)
            startActivity(intent)
        }
        
        // Profile picture click (if exists in layout)
        // binding.profilePicture?.setOnClickListener {
        //     // Already on profile page
        // }
    }
    
    // Navigation methods for bottom navigation
    fun openMainActivity(view: android.view.View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
    
    fun openDashboardActivity(view: android.view.View) {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }
    
    fun openEnergyActivity(view: android.view.View) {
        val intent = Intent(this, EnergyActivity::class.java)
        startActivity(intent)
    }
    
    fun openSecurityActivity(view: android.view.View) {
        val intent = Intent(this, SecurityActivity::class.java)
        startActivity(intent)
    }
    
    fun openAutomationActivity(view: android.view.View) {
        val intent = Intent(this, AutomationActivity::class.java)
        startActivity(intent)
    }

    fun openActivityActivity(view: android.view.View) {
        val intent = Intent(this, ActivityActivity::class.java)
        startActivity(intent)
    }
}
