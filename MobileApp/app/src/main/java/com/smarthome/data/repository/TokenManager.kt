package com.smarthome.data.repository

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val gson = Gson()
    
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs: SharedPreferences = EncryptedSharedPreferences.create(
        context,
        SECURE_PREFS_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    companion object {
        private const val SECURE_PREFS_NAME = "secure_prefs"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_TOKEN_EXPIRES_AT = "token_expires_at"
        private const val KEY_USER_INFO = "user_info"
        private const val KEY_DEVICE_ID = "device_id"
        private const val KEY_SESSION_ID = "session_id"
    }

    suspend fun saveTokens(
        accessToken: String,
        refreshToken: String? = null,
        expiresAt: String? = null
    ) = withContext(Dispatchers.IO) {
        encryptedPrefs.edit().apply {
            putString(KEY_ACCESS_TOKEN, accessToken)
            refreshToken?.let { putString(KEY_REFRESH_TOKEN, it) }
            expiresAt?.let { putString(KEY_TOKEN_EXPIRES_AT, it) }
            putString(KEY_SESSION_ID, generateSessionId())
        }.apply()
    }

    suspend fun getToken(): String? = withContext(Dispatchers.IO) {
        encryptedPrefs.getString(KEY_ACCESS_TOKEN, null)?.let { token ->
            if (isTokenValid(token)) {
                token
            } else {
                clearTokens()
                null
            }
        }
    }

    suspend fun getRefreshToken(): String? = withContext(Dispatchers.IO) {
        encryptedPrefs.getString(KEY_REFRESH_TOKEN, null)
    }

    suspend fun isTokenExpired(): Boolean = withContext(Dispatchers.IO) {
        val expiresAt = encryptedPrefs.getString(KEY_TOKEN_EXPIRES_AT, null)
        if (expiresAt != null) {
            System.currentTimeMillis() > expiresAt.toLong()
        } else {
            true
        }
    }

    suspend fun clearTokens() = withContext(Dispatchers.IO) {
        encryptedPrefs.edit().clear().apply()
    }

    suspend fun saveUserInfo(userInfo: String) = withContext(Dispatchers.IO) {
        encryptedPrefs.edit().putString(KEY_USER_INFO, userInfo).apply()
    }

    suspend fun getUserInfo(): String? = withContext(Dispatchers.IO) {
        encryptedPrefs.getString(KEY_USER_INFO, null)
    }

    suspend fun getDeviceId(): String = withContext(Dispatchers.IO) {
        var deviceId = encryptedPrefs.getString(KEY_DEVICE_ID, null)
        if (deviceId == null) {
            deviceId = generateDeviceId()
            encryptedPrefs.edit().putString(KEY_DEVICE_ID, deviceId).apply()
        }
        deviceId
    }

    private suspend fun isTokenValid(token: String): Boolean = withContext(Dispatchers.IO) {
        // Basic token validation
        token.isNotEmpty() && !isTokenExpired()
    }

    private fun generateSessionId(): String {
        return "session_${System.currentTimeMillis()}_${(1000..9999).random()}"
    }

    private fun generateDeviceId(): String {
        return "device_${System.currentTimeMillis()}_${(10000..99999).random()}"
    }

    // Certificate pinning (simplified version)
    fun getCertificatePins(): List<String> {
        return listOf(
            // Add your certificate hashes here
            // "sha256/AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="
        )
    }

    // Security headers
    fun getSecurityHeaders(): Map<String, String> {
        return mapOf(
            "X-Device-ID" to "device_id_placeholder", // Would be retrieved from secure storage
            "X-App-Version" to "1.0.0",
            "X-Platform" to "android",
            "X-OS-Version" to android.os.Build.VERSION.RELEASE,
            "X-Device-Model" to android.os.Build.MODEL
        )
    }
}
