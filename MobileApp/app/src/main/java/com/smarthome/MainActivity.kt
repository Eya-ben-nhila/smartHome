package com.smarthome

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.AndroidEntryPoint
import com.smarthome.databinding.ActivityMainSimpleBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainSimpleBinding
    private var currentLanguage = DEFAULT_LANGUAGE
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentLanguage = AppPreferences.getLanguage()
        applyLanguage(currentLanguage, recreateActivity = false)
        
        // Check if user is already logged in
        if (AppPreferences.isLoggedIn() && !AppPreferences.isLocalMode()) {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
            return
        }
        
        binding = ActivityMainSimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        updateLanguageToggle()
        
        // Set up click listener for get started button
        binding.getStartedButton.setOnClickListener {
            // Navigate to sign in activity
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

        binding.englishButton.setOnClickListener {
            switchLanguage(DEFAULT_LANGUAGE)
        }

        binding.frenchButton.setOnClickListener {
            switchLanguage(FRENCH_LANGUAGE)
        }
    }

    private fun switchLanguage(language: String) {
        if (language == currentLanguage) return

        currentLanguage = language
        AppPreferences.setLanguage(language)
        applyLanguage(language, recreateActivity = true)
    }

    private fun applyLanguage(language: String, recreateActivity: Boolean) {
        val locales = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(locales)

        if (recreateActivity) {
            recreate()
        }
    }

    private fun updateLanguageToggle() {
        val isEnglish = currentLanguage == DEFAULT_LANGUAGE
        binding.englishButton.backgroundTintList = ColorStateList.valueOf(if (isEnglish) ACTIVE_LANGUAGE_COLOR else INACTIVE_LANGUAGE_COLOR)
        binding.englishButton.setTextColor(if (isEnglish) ACTIVE_TEXT_COLOR else INACTIVE_TEXT_COLOR)
        binding.frenchButton.backgroundTintList = ColorStateList.valueOf(if (isEnglish) INACTIVE_LANGUAGE_COLOR else ACTIVE_LANGUAGE_COLOR)
        binding.frenchButton.setTextColor(if (isEnglish) INACTIVE_TEXT_COLOR else ACTIVE_TEXT_COLOR)
    }

    companion object {
        private const val DEFAULT_LANGUAGE = "en"
        private const val FRENCH_LANGUAGE = "fr"
        private const val ACTIVE_LANGUAGE_COLOR = 0xFF26D1E7.toInt()
        private const val INACTIVE_LANGUAGE_COLOR = 0x33FFFFFF
        private const val ACTIVE_TEXT_COLOR = 0xFFFFFFFF.toInt()
        private const val INACTIVE_TEXT_COLOR = 0xCDFFFFFF.toInt()
    }
}
