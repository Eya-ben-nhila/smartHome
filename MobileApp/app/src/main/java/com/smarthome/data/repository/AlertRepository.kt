package com.smarthome.data.repository

import com.smarthome.data.model.*
import retrofit2.Response

interface AlertRepository {
    suspend fun getAlerts(): Result<List<Alert>>
    suspend fun markAlertAsRead(alertId: String): Result<Unit>
    suspend fun markAllAlertsAsRead(): Result<Unit>
    suspend fun getUnreadAlerts(): Result<List<Alert>>
    suspend fun getAlertsByType(type: String): Result<List<Alert>>
    suspend fun getAlertsBySeverity(severity: String): Result<List<Alert>>
}

class AlertRepositoryImpl(
    private val apiService: ApiService
) : AlertRepository {

    override suspend fun getAlerts(): Result<List<Alert>> {
        return try {
            val response = apiService.getAlerts()
            
            if (response.isSuccessful && response.body() != null) {
                val alertsResponse = response.body()!!
                Result.success(alertsResponse.alerts)
            } else {
                // Fallback to demo alerts
                Result.success(getDemoAlerts())
            }
        } catch (e: Exception) {
            // Network error, fallback to demo alerts
            Result.success(getDemoAlerts())
        }
    }

    override suspend fun markAlertAsRead(alertId: String): Result<Unit> {
        return try {
            // In a real implementation, you would call the API
            // For now, we'll just return success
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markAllAlertsAsRead(): Result<Unit> {
        return try {
            // In a real implementation, you would call the API
            // For now, we'll just return success
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUnreadAlerts(): Result<List<Alert>> {
        return try {
            val allAlerts = getAlerts().getOrNull() ?: return Result.failure(Exception("No alerts available"))
            val unreadAlerts = allAlerts.filter { !it.isRead }
            Result.success(unreadAlerts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAlertsByType(type: String): Result<List<Alert>> {
        return try {
            val allAlerts = getAlerts().getOrNull() ?: return Result.failure(Exception("No alerts available"))
            val filteredAlerts = allAlerts.filter { it.type.equals(type, ignoreCase = true) }
            Result.success(filteredAlerts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAlertsBySeverity(severity: String): Result<List<Alert>> {
        return try {
            val allAlerts = getAlerts().getOrNull() ?: return Result.failure(Exception("No alerts available"))
            val filteredAlerts = allAlerts.filter { it.severity.equals(severity, ignoreCase = true) }
            Result.success(filteredAlerts)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getDemoAlerts(): List<Alert> {
        return listOf(
            Alert(
                id = "alert_1",
                type = "security",
                title = "Motion Detected",
                message = "Motion detected at front door camera",
                severity = "medium",
                timestamp = "2024-01-15T14:30:00Z",
                isRead = false,
                deviceId = "camera_front_door",
                actionUrl = null
            ),
            Alert(
                id = "alert_2",
                type = "energy",
                title = "High Energy Usage",
                message = "Energy usage is 25% higher than usual today",
                severity = "low",
                timestamp = "2024-01-15T12:00:00Z",
                isRead = true,
                deviceId = null,
                actionUrl = "/energy"
            ),
            Alert(
                id = "alert_3",
                type = "device",
                title = "Device Offline",
                message = "Living Room thermostat is offline",
                severity = "high",
                timestamp = "2024-01-15T10:15:00Z",
                isRead = false,
                deviceId = "thermostat_living_room",
                actionUrl = "/devices/thermostat_living_room"
            ),
            Alert(
                id = "alert_4",
                type = "maintenance",
                title = "Filter Replacement Due",
                message = "HVAC filter needs replacement in 5 days",
                severity = "low",
                timestamp = "2024-01-15T09:00:00Z",
                isRead = true,
                deviceId = "hvac_system",
                actionUrl = "/maintenance"
            )
        )
    }
}
