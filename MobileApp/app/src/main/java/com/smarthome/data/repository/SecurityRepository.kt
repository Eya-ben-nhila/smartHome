package com.smarthome.data.repository

import com.smarthome.data.model.*
import retrofit2.Response

interface SecurityRepository {
    suspend fun getSecurityEvents(): Result<List<SecurityEvent>>
    suspend fun getSecurityStatus(): Result<List<SecurityStatus>>
    suspend fun acknowledgeEvent(eventId: String): Result<Unit>
    suspend fun getUnacknowledgedEvents(): Result<List<SecurityEvent>>
    suspend fun getEventsByType(type: String): Result<List<SecurityEvent>>
    suspend fun getEventsBySeverity(severity: String): Result<List<SecurityEvent>>
    suspend fun getCachedSecurityEvents(): Result<List<SecurityEvent>>
    suspend fun cacheSecurityEvents(events: List<SecurityEvent>)
}

class SecurityRepositoryImpl(
    private val apiService: ApiService,
    private val securityDao: com.smarthome.data.database.SecurityDao
) : SecurityRepository {

    override suspend fun getSecurityEvents(): Result<List<SecurityEvent>> {
        return try {
            val response = apiService.getSecurityEvents()
            
            if (response.isSuccessful && response.body() != null) {
                val securityResponse = response.body()!!
                
                // Cache security events locally
                cacheSecurityEvents(securityResponse.events)
                
                Result.success(securityResponse.events)
            } else {
                // Fallback to cached events
                val cachedEvents = getCachedSecurityEvents()
                if (cachedEvents.isSuccess && cachedEvents.getOrNull()!!.isNotEmpty()) {
                    Result.success(cachedEvents.getOrThrow())
                } else {
                    Result.success(getDemoSecurityEvents())
                }
            }
        } catch (e: Exception) {
            // Network error, fallback to cached events
            val cachedEvents = getCachedSecurityEvents()
            if (cachedEvents.isSuccess && cachedEvents.getOrNull()!!.isNotEmpty()) {
                Result.success(cachedEvents.getOrThrow())
            } else {
                Result.success(getDemoSecurityEvents())
            }
        }
    }

    override suspend fun getSecurityStatus(): Result<List<SecurityStatus>> {
        return try {
            val response = apiService.getSecurityEvents()
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.status)
            } else {
                Result.success(getDemoSecurityStatus())
            }
        } catch (e: Exception) {
            Result.success(getDemoSecurityStatus())
        }
    }

    override suspend fun acknowledgeEvent(eventId: String): Result<Unit> {
        return try {
            // Update local cache
            securityDao.acknowledgeEvent(
                eventId = eventId,
                isAcknowledged = true,
                acknowledgedBy = "user",
                acknowledgedAt = java.time.Instant.now().toString()
            )
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUnacknowledgedEvents(): Result<List<SecurityEvent>> {
        return try {
            val response = apiService.getSecurityEvents()
            
            if (response.isSuccessful && response.body() != null) {
                val unacknowledged = response.body()!!.events.filter { !it.isAcknowledged }
                Result.success(unacknowledged)
            } else {
                // Fallback to cached unacknowledged events
                val cachedEvents = securityDao.getUnacknowledgedEvents(false)
                Result.success(cachedEvents.map { it.toSecurityEvent() })
            }
        } catch (e: Exception) {
            // Fallback to cached events
            val cachedEvents = securityDao.getUnacknowledgedEvents(false)
            Result.success(cachedEvents.map { it.toSecurityEvent() })
        }
    }

    override suspend fun getEventsByType(type: String): Result<List<SecurityEvent>> {
        return try {
            val allEvents = getSecurityEvents().getOrNull() ?: return Result.failure(Exception("No events available"))
            val filteredEvents = allEvents.filter { it.type.equals(type, ignoreCase = true) }
            Result.success(filteredEvents)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEventsBySeverity(severity: String): Result<List<SecurityEvent>> {
        return try {
            val allEvents = getSecurityEvents().getOrNull() ?: return Result.failure(Exception("No events available"))
            val filteredEvents = allEvents.filter { it.severity.equals(severity, ignoreCase = true) }
            Result.success(filteredEvents)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCachedSecurityEvents(): Result<List<SecurityEvent>> {
        return try {
            val cachedEvents = securityDao.getSecurityEvents(50)
            Result.success(cachedEvents.map { it.toSecurityEvent() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cacheSecurityEvents(events: List<SecurityEvent>) {
        try {
            val eventEntities = events.map { event ->
                com.smarthome.data.database.entities.SecurityEventEntity(
                    id = event.id,
                    deviceId = event.deviceId,
                    deviceName = event.deviceName,
                    type = event.type,
                    event = event.event,
                    severity = event.severity,
                    timestamp = event.timestamp,
                    location = event.location,
                    description = event.description,
                    isAcknowledged = event.isAcknowledged,
                    acknowledgedBy = null,
                    acknowledgedAt = null,
                    isResolved = false,
                    resolvedBy = null,
                    resolvedAt = null,
                    imageUrl = event.imageUrl,
                    videoUrl = event.videoUrl,
                    confidence = null,
                    metadata = emptyMap(),
                    isCached = true,
                    cacheTimestamp = System.currentTimeMillis()
                )
            }
            
            securityDao.insertSecurityEvents(eventEntities)
        } catch (e: Exception) {
            // Handle cache error silently
        }
    }

    private fun getDemoSecurityEvents(): List<SecurityEvent> {
        return listOf(
            SecurityEvent(
                id = "security_event_1",
                deviceId = "camera_front_door",
                deviceName = "Front Door Camera",
                type = "camera",
                event = "motion_detected",
                severity = "medium",
                timestamp = "2024-01-15T14:30:00Z",
                location = "Front Door",
                description = "Motion detected at front door",
                isAcknowledged = false,
                imageUrl = null,
                videoUrl = null
            ),
            SecurityEvent(
                id = "security_event_2",
                deviceId = "sensor_back_door",
                deviceName = "Back Door Sensor",
                type = "door",
                event = "opened",
                severity = "low",
                timestamp = "2024-01-15T13:15:00Z",
                location = "Back Door",
                description = "Back door opened",
                isAcknowledged = true,
                imageUrl = null,
                videoUrl = null
            )
        )
    }

    private fun getDemoSecurityStatus(): List<SecurityStatus> {
        return listOf(
            SecurityStatus(
                area = "front",
                status = "armed",
                activeDevices = 3,
                totalDevices = 4,
                lastActivity = "2024-01-15T14:30:00Z"
            ),
            SecurityStatus(
                area = "back",
                status = "disarmed",
                activeDevices = 0,
                totalDevices = 2,
                lastActivity = "2024-01-15T13:15:00Z"
            ),
            SecurityStatus(
                area = "interior",
                status = "armed",
                activeDevices = 2,
                totalDevices = 3,
                lastActivity = "2024-01-15T12:00:00Z"
            )
        )
    }
}

// Extension function for mapping
private fun com.smarthome.data.database.entities.SecurityEventEntity.toSecurityEvent(): SecurityEvent {
    return SecurityEvent(
        id = this.id,
        deviceId = this.deviceId,
        deviceName = this.deviceName,
        type = this.type,
        event = this.event,
        severity = this.severity,
        timestamp = this.timestamp,
        location = this.location,
        description = this.description,
        isAcknowledged = this.isAcknowledged,
        imageUrl = this.imageUrl,
        videoUrl = this.videoUrl
    )
}
