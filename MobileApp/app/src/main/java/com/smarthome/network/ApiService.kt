package com.smarthome.network

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ApiService(private val context: Context) {
    
    companion object {
        private const val BASE_URL = "http://10.0.2.2:8080" // For Android emulator
        // For real device, use your computer's IP: "http://192.168.1.100:8080"
        
        // Alternative endpoints for testing
        private const val FALLBACK_BASE_URL = "http://localhost:8080"
    }
    
    // Login endpoint
    suspend fun login(email: String, password: String): Result<JSONObject> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE_URL/mobile/login")
            val connection = url.openConnection() as HttpURLConnection
            
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.doOutput = true
            
            val requestBody = JSONObject().apply {
                put("email", email)
                put("password", password)
            }.toString()
            
            connection.outputStream.write(requestBody.toByteArray())
            
            val responseCode = connection.responseCode
            val response = if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val responseText = reader.readText()
                JSONObject(responseText)
            } else {
                JSONObject().apply {
                    put("error", "HTTP $responseCode")
                    put("message", "Login failed")
                }
            }
            
            Result.success(response)
        } catch (e: Exception) {
            Log.e("ApiService", "Login error", e)
            Result.failure(e)
        }
    }
    
    // Get devices
    suspend fun getDevices(): Result<JSONObject> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE_URL/mobile/devices")
            val connection = url.openConnection() as HttpURLConnection
            
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")
            
            val responseCode = connection.responseCode
            val response = if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val responseText = reader.readText()
                JSONObject(responseText)
            } else {
                JSONObject().apply {
                    put("success", false)
                    put("devices", emptyList<Any>())
                    put("error", "HTTP $responseCode")
                }
            }
            
            Result.success(response)
        } catch (e: Exception) {
            Log.e("ApiService", "Get devices error", e)
            // Return empty result on error
            Result.success(JSONObject().apply {
                put("success", true)
                put("devices", emptyList<Any>())
                put("total", 0)
            })
        }
    }
    
    // Update device status
    suspend fun updateDeviceStatus(deviceId: String, status: String): Result<JSONObject> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE_URL/mobile/devices/$deviceId/status")
            val connection = url.openConnection() as HttpURLConnection
            
            connection.requestMethod = "PUT"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("Accept", "application/json")
            connection.doOutput = true
            
            val requestBody = JSONObject().apply {
                put("status", status)
            }.toString()
            
            connection.outputStream.write(requestBody.toByteArray())
            
            val responseCode = connection.responseCode
            val response = if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val responseText = reader.readText()
                JSONObject(responseText)
            } else {
                JSONObject().apply {
                    put("success", false)
                    put("error", "HTTP $responseCode")
                }
            }
            
            Result.success(response)
        } catch (e: Exception) {
            Log.e("ApiService", "Update device status error", e)
            Result.failure(e)
        }
    }
    
    // Get energy data
    suspend fun getEnergyData(): Result<JSONObject> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE_URL/mobile/energy")
            val connection = url.openConnection() as HttpURLConnection
            
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")
            
            val responseCode = connection.responseCode
            val response = if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val responseText = reader.readText()
                JSONObject(responseText)
            } else {
                JSONObject().apply {
                    put("success", false)
                    put("error", "HTTP $responseCode")
                }
            }
            
            Result.success(response)
        } catch (e: Exception) {
            Log.e("ApiService", "Get energy data error", e)
            Result.failure(e)
        }
    }
    
    // Get security events
    suspend fun getSecurityEvents(): Result<JSONObject> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE_URL/mobile/security")
            val connection = url.openConnection() as HttpURLConnection
            
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")
            
            val responseCode = connection.responseCode
            val response = if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val responseText = reader.readText()
                JSONObject(responseText)
            } else {
                JSONObject().apply {
                    put("success", false)
                    put("events", emptyList<Any>())
                    put("error", "HTTP $responseCode")
                }
            }
            
            Result.success(response)
        } catch (e: Exception) {
            Log.e("ApiService", "Get security events error", e)
            Result.failure(e)
        }
    }
    
    // Get automations
    suspend fun getAutomations(): Result<JSONObject> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE_URL/mobile/automations")
            val connection = url.openConnection() as HttpURLConnection
            
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")
            
            val responseCode = connection.responseCode
            val response = if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val responseText = reader.readText()
                JSONObject(responseText)
            } else {
                JSONObject().apply {
                    put("success", false)
                    put("automations", emptyList<Any>())
                    put("error", "HTTP $responseCode")
                }
            }
            
            Result.success(response)
        } catch (e: Exception) {
            Log.e("ApiService", "Get automations error", e)
            Result.failure(e)
        }
    }
    
    // Get profile
    suspend fun getProfile(): Result<JSONObject> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE_URL/mobile/profile")
            val connection = url.openConnection() as HttpURLConnection
            
            connection.requestMethod = "GET"
            connection.setRequestProperty("Accept", "application/json")
            
            val responseCode = connection.responseCode
            val response = if (responseCode == HttpURLConnection.HTTP_OK) {
                val reader = BufferedReader(InputStreamReader(connection.inputStream))
                val responseText = reader.readText()
                JSONObject(responseText)
            } else {
                JSONObject().apply {
                    put("success", false)
                    put("error", "HTTP $responseCode")
                }
            }
            
            Result.success(response)
        } catch (e: Exception) {
            Log.e("ApiService", "Get profile error", e)
            Result.failure(e)
        }
    }
    
    // Health check
    suspend fun healthCheck(): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE_URL/mobile/health")
            val connection = url.openConnection() as HttpURLConnection
            
            connection.requestMethod = "GET"
            connection.connectTimeout = 5000
            connection.readTimeout = 5000
            
            val responseCode = connection.responseCode
            Result.success(responseCode == HttpURLConnection.HTTP_OK)
        } catch (e: Exception) {
            Log.e("ApiService", "Health check error", e)
            Result.success(false)
        }
    }
}
