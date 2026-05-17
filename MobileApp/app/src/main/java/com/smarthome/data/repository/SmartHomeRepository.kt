package com.smarthome.data.repository

import com.smarthome.data.api.NetworkClient
import com.smarthome.data.api.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SmartHomeRepository {
    
    private val apiService = NetworkClient.apiService
    
    // Authentication
    suspend fun register(fullName: String, email: String, password: String): Result<BackendAuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = BackendRegisterRequest(fullName, email, password)
                val response = apiService.register(request)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Registration failed: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun login(email: String, password: String): Result<BackendAuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val request = BackendLoginRequest(email, password)
                val response = apiService.login(request)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Login failed: ${response.message()}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun healthCheck(): Result<String> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.healthCheck()
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!["status"] ?: "OK")
                } else {
                    Result.failure(Exception("Health check failed"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    // Device Management
    suspend fun getAllDevices(token: String): Result<List<BackendDevice>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllDevices("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to get devices"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun createDevice(token: String, device: BackendDeviceRequest): Result<BackendDevice> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.createDevice("Bearer $token", device)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to create device"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun updateDevice(token: String, deviceId: String, device: BackendDeviceRequest): Result<BackendDevice> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.updateDevice("Bearer $token", deviceId, device)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to update device"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun deleteDevice(token: String, deviceId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.deleteDevice("Bearer $token", deviceId)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to delete device"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun updateDeviceStatus(token: String, deviceId: String, status: String): Result<BackendDevice> {
        return withContext(Dispatchers.IO) {
            try {
                val request = BackendDeviceStatusRequest(status)
                val response = apiService.updateDeviceStatus("Bearer $token", deviceId, request)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to update device status"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun sendDeviceCommand(token: String, deviceId: String, command: String, data: Map<String, Any>? = null): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val request = BackendDeviceCommandRequest(command, data)
                val response = apiService.sendDeviceCommand("Bearer $token", deviceId, request)
                if (response.isSuccessful) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to send device command"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun getDeviceStats(token: String): Result<BackendDeviceStats> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getDeviceStats("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to get device stats"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    // Energy Management
    suspend fun getEnergyDashboard(token: String): Result<Map<String, Any>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getEnergyDashboard("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to get energy dashboard"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    // Automation Management
    suspend fun getAllAutomations(token: String): Result<List<Map<String, Any>>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAllAutomations("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to get automations"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun createAutomation(token: String, automation: Map<String, Any>): Result<Map<String, Any>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.createAutomation("Bearer $token", automation)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to create automation"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun toggleAutomation(token: String, automationId: String): Result<Map<String, Any>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.toggleAutomation("Bearer $token", automationId)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to toggle automation"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    // Security/Alerts
    suspend fun getAlerts(token: String): Result<List<Map<String, Any>>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getAlerts("Bearer $token")
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to get alerts"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
    
    suspend fun resolveAlert(token: String, alertId: String): Result<Map<String, Any>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.resolveAlert("Bearer $token", alertId)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception("Failed to resolve alert"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
