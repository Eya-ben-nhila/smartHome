package com.smarthome

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smarthome.data.repository.BackendRequestException
import com.smarthome.data.repository.SmartHomeRepository
import dagger.hilt.android.AndroidEntryPoint
import com.smarthome.databinding.ActivitySignupSimpleBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import java.io.IOException

@AndroidEntryPoint
class SignupActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySignupSimpleBinding
    private val repository = SmartHomeRepository()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupSimpleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Set up click listeners
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        // Sign up button click
        binding.signupButton?.setOnClickListener {
            val fullName = binding.fullNameInput.text.toString().trim()
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString()
            val confirmPassword = binding.confirmPasswordInput.text.toString()

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                android.widget.Toast.makeText(this, getString(R.string.auth_missing_signup_fields), android.widget.Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            binding.signupButton?.isEnabled = false

            lifecycleScope.launch {
                try {
                    val result = withTimeoutOrNull(SIGNUP_TIMEOUT_MS) {
                        repository.register(fullName, email, password)
                    }

                    if (result == null) {
                        completeLocalSignup(email, password, fullName)
                        return@launch
                    }

                    result.onSuccess {
                        val loginResult = withTimeoutOrNull(SIGNUP_TIMEOUT_MS) {
                            repository.login(email, password)
                        }

                        if (loginResult == null) {
                            completeLocalSignup(email, password, fullName)
                            return@launch
                        }

                        loginResult.onSuccess { authResponse ->
                            AppPreferences.setLocalMode(false)
                            authResponse.token?.let { AppPreferences.setJwtToken(it) }
                            authResponse.userId?.let { AppPreferences.setUserId(it) }
                            authResponse.user?.fullName?.let { AppPreferences.setUserName(it) }
                            AppPreferences.saveLoginSession(email, password, rememberMe = true, name = authResponse.user?.fullName ?: fullName)
                            android.widget.Toast.makeText(this@SignupActivity, getString(R.string.auth_signup_success), android.widget.Toast.LENGTH_SHORT).show()
                            openDashboard()
                        }.onFailure { error ->
                            android.util.Log.e("SignupActivity", "Post-registration login error", error)
                            if (error is IOException && error !is BackendRequestException) {
                                completeLocalSignup(email, password, fullName)
                            } else {
                                android.widget.Toast.makeText(
                                    this@SignupActivity,
                                    getString(R.string.auth_signup_failed, error.message ?: ""),
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }.onFailure { error ->
                        android.util.Log.e("SignupActivity", "Registration error", error)
                        if (error is IOException && error !is BackendRequestException) {
                            completeLocalSignup(email, password, fullName)
                        } else {
                            android.widget.Toast.makeText(
                                this@SignupActivity,
                                getString(R.string.auth_signup_failed, error.message ?: ""),
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } finally {
                    if (!isFinishing) {
                        binding.signupButton?.isEnabled = true
                    }
                }
            }
        }
        
        // Sign in link click
        binding.signinLink?.setOnClickListener {
            // Navigate back to sign in activity
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
            finish() // Close sign up activity
        }
    }

    private fun completeLocalSignup(email: String, password: String, fullName: String) {
        AppPreferences.setLocalMode(true)
        AppPreferences.saveLoginSession(email, password, rememberMe = true, name = fullName)
        android.widget.Toast.makeText(this, getString(R.string.auth_signup_offline), android.widget.Toast.LENGTH_SHORT).show()
        openDashboard()
    }

    private fun openDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }

    companion object {
        private const val SIGNUP_TIMEOUT_MS = 8_000L
    }
}
