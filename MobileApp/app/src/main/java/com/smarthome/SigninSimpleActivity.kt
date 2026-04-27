package com.smarthome

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SigninSimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin_simple)
        
        // Set action bar title
        supportActionBar?.title = "Sign In"
        
        // Hide action bar for login screen
        supportActionBar?.hide()
    }
}
