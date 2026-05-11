package com.smarthome

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SmartHomeApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
    }
}
