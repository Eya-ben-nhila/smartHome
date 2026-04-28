package com.smarthome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class TestActivity : AppCompatActivity() {
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Create a simple layout programmatically to avoid XML issues
        val textView = android.widget.TextView(this).apply {
            text = "Test Activity - If you see this, the app works!"
            textSize = 20f
            setPadding(50, 50, 50, 50)
        }
        
        setContentView(textView)
    }
}
