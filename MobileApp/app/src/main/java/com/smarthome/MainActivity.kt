package com.smarthome

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        try {
            // Try to use the original XML layout
            setContentView(R.layout.activity_main_simple)
            setupOriginalNavigation()
        } catch (e: Exception) {
            // Fallback to simple layout if XML fails
            createFallbackLayout()
            Toast.makeText(this, "Using fallback layout - original layout failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
    
    private fun setupOriginalNavigation() {
        try {
            // Get Started button - navigate to login
            findViewById<Button>(R.id.getStartedButton)?.setOnClickListener {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            
            // Profile click on textView
            findViewById<TextView>(R.id.textView)?.setOnClickListener {
                Toast.makeText(this, "Profile clicked!", Toast.LENGTH_SHORT).show()
            }
            
        } catch (e: Exception) {
            Toast.makeText(this, "Some features unavailable", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun createFallbackLayout() {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }
        
        val titleText = TextView(this).apply {
            text = "SmartHome App (Fallback)"
            textSize = 24f
            setPadding(0, 0, 0, 50)
        }
        
        val loginButton = Button(this).apply {
            text = "Go to Login"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            }
        }
        
        val securityButton = Button(this).apply {
            text = "Security"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, SecuritySimpleActivity::class.java))
            }
        }
        
        val energyButton = Button(this).apply {
            text = "Energy"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, EnergySimpleActivity::class.java))
            }
        }
        
        val activityButton = Button(this).apply {
            text = "Activity"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, ActivitySimpleActivity::class.java))
            }
        }
        
        val automationButton = Button(this).apply {
            text = "Automation"
            setOnClickListener {
                startActivity(Intent(this@MainActivity, AutomationSimpleActivity::class.java))
            }
        }
        
        layout.addView(titleText)
        layout.addView(loginButton)
        layout.addView(securityButton)
        layout.addView(energyButton)
        layout.addView(activityButton)
        layout.addView(automationButton)
        setContentView(layout)
    }
}
