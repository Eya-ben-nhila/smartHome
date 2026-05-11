package com.smarthome

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AppPreferences {
    
    private const val PREFS_NAME = "SmartHomePrefs"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_EMAIL = "email"
    private const val KEY_PASSWORD = "password"
    private const val KEY_NAME = "name"
    private val KEY_REMEMBER_ME = "remember_me"
    
    private lateinit var prefs: SharedPreferences
    
    fun init(context: Context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    // Save login session
    fun saveLoginSession(email: String, password: String, rememberMe: Boolean, name: String = "") {
        try {
            val editor = prefs.edit()
            editor.putBoolean(KEY_IS_LOGGED_IN, true)
            editor.putString(KEY_EMAIL, email)
            editor.putString(KEY_PASSWORD, password)
            editor.putString(KEY_NAME, name)
            editor.putBoolean(KEY_REMEMBER_ME, rememberMe)
            val success = editor.commit()
            
            android.util.Log.d("AppPreferences", "Saved session: email=$email, rememberMe=$rememberMe, name=$name, success=$success")
        } catch (e: Exception) {
            android.util.Log.e("AppPreferences", "Failed to save session", e)
        }
    }
    
    // Getters
    fun isLoggedIn(): Boolean {
        return try {
            prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        } catch (e: Exception) {
            android.util.Log.e("AppPreferences", "Failed to check login status", e)
            false
        }
    }
    
    fun getUserEmail(): String? {
        return try {
            val email = prefs.getString(KEY_EMAIL, null)
            android.util.Log.d("AppPreferences", "Retrieved email: $email")
            email
        } catch (e: Exception) {
            android.util.Log.e("AppPreferences", "Failed to get email", e)
            null
        }
    }
    
    fun getUserPassword(): String? {
        return try {
            val password = prefs.getString(KEY_PASSWORD, null)
            android.util.Log.d("AppPreferences", "Retrieved password: ${if (password.isNullOrEmpty()) "null/empty" else "***"}")
            password
        } catch (e: Exception) {
            android.util.Log.e("AppPreferences", "Failed to get password", e)
            null
        }
    }
    
    fun getUserName(): String? {
        return try {
            val name = prefs.getString(KEY_NAME, null)
            android.util.Log.d("AppPreferences", "Retrieved name: $name")
            name
        } catch (e: Exception) {
            android.util.Log.e("AppPreferences", "Failed to get name", e)
            null
        }
    }
    
    fun setUserName(name: String) {
        try {
            val editor = prefs.edit()
            editor.putString(KEY_NAME, name)
            val success = editor.commit()
            android.util.Log.d("AppPreferences", "Set name: $name, success=$success")
        } catch (e: Exception) {
            android.util.Log.e("AppPreferences", "Failed to set name", e)
        }
    }
    
    fun shouldRememberMe(): Boolean {
        return try {
            prefs.getBoolean(KEY_REMEMBER_ME, false)
        } catch (e: Exception) {
            android.util.Log.e("AppPreferences", "Failed to check remember me", e)
            false
        }
    }
    
    // Clear credentials
    fun clearCredentials() {
        try {
            val editor = prefs.edit()
            editor.remove(KEY_EMAIL)
            editor.remove(KEY_PASSWORD)
            editor.putBoolean(KEY_REMEMBER_ME, false)
            val success = editor.commit()
            android.util.Log.d("AppPreferences", "Cleared credentials, success=$success")
        } catch (e: Exception) {
            android.util.Log.e("AppPreferences", "Failed to clear credentials", e)
        }
    }
    
    // Logout but keep credentials if remember me is enabled
    fun logout() {
        try {
            val editor = prefs.edit()
            
            // Only clear specific login session data if not remembered
            if (!shouldRememberMe()) {
                editor.remove(KEY_EMAIL)
                editor.remove(KEY_PASSWORD)
                editor.remove(KEY_NAME)
                editor.putBoolean(KEY_REMEMBER_ME, false)
            }
            
            editor.putBoolean(KEY_IS_LOGGED_IN, false)
            val success = editor.commit()
            android.util.Log.d("AppPreferences", "Logged out. Success=$success")
        } catch (e: Exception) {
            android.util.Log.e("AppPreferences", "Failed to logout", e)
        }
    }
    
    fun setAlertAcquitted(alertId: String, isAcquitted: Boolean) {
        try {
            val editor = prefs.edit()
            editor.putBoolean("alert_acquitted_$alertId", isAcquitted)
            val success = editor.commit()
            android.util.Log.d("AppPreferences", "Saved alert state: id=$alertId, state=$isAcquitted, success=$success")
        } catch (e: Exception) {
            android.util.Log.e("AppPreferences", "Failed to set alert status", e)
        }
    }
    
    fun isAlertAcquitted(alertId: String): Boolean {
        return try {
            prefs.getBoolean("alert_acquitted_$alertId", false)
        } catch (e: Exception) {
            false
        }
    }

    // Test SharedPreferences
    fun testSharedPreferences(): Boolean {
        return try {
            val testKey = "test_key"
            val testValue = "test_value_${System.currentTimeMillis()}"
            
            // Write test value
            prefs.edit().putString(testKey, testValue).commit()
            
            // Read test value
            val retrievedValue = prefs.getString(testKey, null)
            
            // Clean up test
            prefs.edit().remove(testKey).commit()
            
            android.util.Log.d("AppPreferences", "SharedPreferences test: wrote=$testValue, read=$retrievedValue, success=${retrievedValue == testValue}")
            
            retrievedValue == testValue
        } catch (e: Exception) {
            android.util.Log.e("AppPreferences", "SharedPreferences test failed", e)
            false
        }
    }
}
