package com.smarthome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SignupSimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_simple)
        
        // Set action bar title
        supportActionBar?.title = "Sign Up"
        
        // Hide action bar for signup screen
        supportActionBar?.hide()
    }
}
