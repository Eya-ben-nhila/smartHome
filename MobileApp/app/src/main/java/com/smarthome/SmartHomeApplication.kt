package com.smarthome

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SmartHomeApplication : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize AppPreferences for global state management
        AppPreferences.init(this)
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(AppPreferences.getLanguage()))
        android.util.Log.d("SmartHomeApplication", "AppPreferences initialized")
    }
}
