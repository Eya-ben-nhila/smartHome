package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    
    private lateinit var profilePopupWindow: PopupWindow
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Setup all click listeners
        setupNavigation()
    }
    
    private fun setupNavigation() {
        // Language button click
        findViewById<ImageButton>(R.id.btnLanguage)?.setOnClickListener {
            // TODO: Show language selection dialog
        }
        
        // Energy button click
        findViewById<LinearLayout>(R.id.energyNavButton)?.setOnClickListener {
            val intent = Intent(this, EnergyActivity::class.java)
            startActivity(intent)
        }
        
        // User profile click - show popup menu
        findViewById<LinearLayout>(R.id.userProfile)?.setOnClickListener {
            Toast.makeText(this, "Profile clicked!", Toast.LENGTH_SHORT).show()
            showProfileMenu(it)
        }
        
        // Main login/enter button click
        findViewById<Button>(R.id.loginButton)?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
        
        // Swipe hint click (treat as enter button)
        findViewById<LinearLayout>(R.id.bottomHint)?.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
    
    private fun showProfileMenu(anchorView: View) {
        val options = arrayOf("Profile", "Settings")
        
        AlertDialog.Builder(this)
            .setTitle("Menu")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> startActivity(Intent(this, ProfileActivity::class.java))
                    1 -> startActivity(Intent(this, SettingsActivity::class.java))
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
