package com.smarthome

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ProfileSimpleActivity : AppCompatActivity() {
    
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var nameInput: android.widget.EditText
    private lateinit var emailInput: android.widget.EditText
    private lateinit var profileImageView: ImageView
    private lateinit var changePhotoText: TextView
    private lateinit var editButton: Button
    private var isEditing = false
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_simple)
        
        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("LoginPrefs", Context.MODE_PRIVATE)
        
        // Initialize views
        nameInput = findViewById(R.id.profileNameInput)
        emailInput = findViewById(R.id.profileEmailInput)
        profileImageView = findViewById(R.id.profileImageView)
        changePhotoText = findViewById(R.id.changePhotoText)
        editButton = findViewById(R.id.editProfileButton)
        
        // Load saved data
        loadProfileData()
        
        // Setup button click listeners
        setupButtonListeners()
        
        // Setup navigation
        setupNavigation()
    }
    
    private fun loadProfileData() {
        val savedName = sharedPreferences.getString("user_name", "John Doe")
        val savedEmail = sharedPreferences.getString("user_email", "john.doe@smarthome.com")
        val savedPhoto = sharedPreferences.getInt("user_photo", R.drawable.profile)
        
        nameInput.setText(savedName)
        emailInput.setText(savedEmail)
        profileImageView.setBackgroundResource(savedPhoto)
    }
    
    private fun setupButtonListeners() {
        editButton.setOnClickListener {
            if (!isEditing) {
                // Enter edit mode
                isEditing = true
                nameInput.isEnabled = true
                emailInput.isEnabled = true
                changePhotoText.visibility = View.VISIBLE
                nameInput.requestFocus()
                editButton.text = "Save Changes"
                editButton.setBackgroundColor(android.graphics.Color.parseColor("#4CAF50"))
                Toast.makeText(this, "Editing enabled", Toast.LENGTH_SHORT).show()
            } else {
                // Confirm save
                showSaveConfirmationDialog()
            }
        }

        profileImageView.setOnClickListener {
            if (isEditing) {
                showPhotoPicker()
            }
        }
        
        // Logout button
        findViewById<Button>(R.id.logoutButton)?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun showPhotoPicker() {
        val photos = arrayOf("Default Profile", "Alternative Profile", "App Icon")
        val drawableIds = intArrayOf(R.drawable.profile, R.drawable.profilepic, R.drawable.ic_launcher_foreground)
        
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Choose Profile Picture")
            .setItems(photos) { _, which ->
                val selectedDrawable = drawableIds[which]
                profileImageView.setBackgroundResource(selectedDrawable)
                // Temporarily store selected photo in a tag or variable
                profileImageView.tag = selectedDrawable
            }
            .show()
    }
    
    private fun showSaveConfirmationDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Confirm Changes")
            .setMessage("Are you sure you want to save these changes to your profile?")
            .setPositiveButton("Yes") { _, _ ->
                saveProfileData()
            }
            .setNegativeButton("No", null)
            .show()
    }
    
    private fun saveProfileData() {
        val newName = nameInput.text.toString().trim()
        val newEmail = emailInput.text.toString().trim()
        val selectedPhoto = profileImageView.tag as? Int ?: sharedPreferences.getInt("user_photo", R.drawable.profile)
        
        if (newName.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Save to SharedPreferences
        sharedPreferences.edit().apply {
            putString("user_name", newName)
            putString("user_email", newEmail)
            putInt("user_photo", selectedPhoto)
            apply()
        }
        
        // Exit edit mode
        isEditing = false
        nameInput.isEnabled = false
        emailInput.isEnabled = false
        changePhotoText.visibility = View.GONE
        editButton.text = "Edit Profile"
        editButton.setBackgroundColor(android.graphics.Color.parseColor("#2196F3"))
        
        // Show success dialog
        showSuccessDialog()
    }
    
    private fun showSuccessDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Success")
            .setMessage("Your profile has been updated successfully!")
            .setPositiveButton("OK", null)
            .show()
    }
    
    private fun setupNavigation() {
        try {
            val navigationLayout = findViewById<LinearLayout>(R.id.navigation)
            if (navigationLayout != null) {
                // Home button
                navigationLayout.findViewById<LinearLayout>(R.id.homeNavButton)?.setOnClickListener {
                    val intent = Intent(this, DashboardFinalActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                
                // Security button
                navigationLayout.findViewById<LinearLayout>(R.id.securityNavButton)?.setOnClickListener {
                    startActivity(Intent(this, SecuritySimpleActivity::class.java))
                    finish()
                }
                
                // Energy button
                navigationLayout.findViewById<LinearLayout>(R.id.energyNavButton)?.setOnClickListener {
                    startActivity(Intent(this, EnergySimpleActivity::class.java))
                    finish()
                }
                
                // Activity button
                navigationLayout.findViewById<LinearLayout>(R.id.activityNavButton)?.setOnClickListener {
                    startActivity(Intent(this, ActivitySimpleActivity::class.java))
                    finish()
                }
                
                // Automation button
                navigationLayout.findViewById<LinearLayout>(R.id.automationsNavButton)?.setOnClickListener {
                    startActivity(Intent(this, AutomationSimpleActivity::class.java))
                    finish()
                }
                
                // Alerts button
                navigationLayout.findViewById<LinearLayout>(R.id.alertsNavButton)?.setOnClickListener {
                    startActivity(Intent(this, AlertsSimpleActivity::class.java))
                    finish()
                }
            }
        } catch (e: Exception) {
            // Navigation setup error
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    
    // Extension for double click listener if needed elsewhere
    private fun View.setOnDoubleClickListener(onDoubleClick: () -> Unit) {
        var lastClickTime: Long = 0
        val doubleClickDelay: Long = 300 // ms
        
        this.setOnClickListener {
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastClickTime < doubleClickDelay) {
                onDoubleClick()
            }
            lastClickTime = currentTime
        }
    }
}
