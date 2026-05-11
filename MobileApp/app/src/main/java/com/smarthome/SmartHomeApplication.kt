package com.smarthome

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SmartHomeApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize AppPreferences for global state management
        AppPreferences.init(this)
        android.util.Log.d("SmartHomeApplication", "AppPreferences initialized")
    }
}
