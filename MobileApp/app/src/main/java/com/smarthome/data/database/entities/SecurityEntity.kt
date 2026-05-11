package com.smarthome.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "security_events",
    indices = [
        Index(value = ["deviceId"]),
        Index(value = ["type"]),
        Index(value = ["timestamp"]),
        Index(value = ["severity"])
    ]
)
data class SecurityEventEntity(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    
    @SerializedName("deviceId")
    val deviceId: String?,
    
    @SerializedName("deviceName")
    val deviceName: String?,
    
    @SerializedName("type")
    val type: String, // motion, door, window, alarm, camera, smoke, leak
    
    @SerializedName("event")
    val event: String, // detected, opened, closed, triggered, recording, alert
    
    @SerializedName("severity")
    val severity: String, // low, medium, high, critical
    
    @SerializedName("timestamp")
    val timestamp: String,
    
    @SerializedName("location")
    val location: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("isAcknowledged")
    val isAcknowledged: Boolean,
    
    @SerializedName("acknowledgedBy")
    val acknowledgedBy: String?,
    
    @SerializedName("acknowledgedAt")
    val acknowledgedAt: String?,
    
    @SerializedName("isResolved")
    val isResolved: Boolean,
    
    @SerializedName("resolvedBy")
    val resolvedBy: String?,
    
    @SerializedName("resolvedAt")
    val resolvedAt: String?,
    
    @SerializedName("imageUrl")
    val imageUrl: String?,
    
    @SerializedName("videoUrl")
    val videoUrl: String?,
    
    @SerializedName("confidence")
    val confidence: Double?, // AI detection confidence
    
    @SerializedName("metadata")
    val metadata: Map<String, Any> = emptyMap(),
    
    // Local fields
    val isCached: Boolean = false,
    val cacheTimestamp: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "security_status",
    indices = [
        Index(value = ["area"]),
        Index(value = ["timestamp"])
    ]
)
data class SecurityStatusEntity(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    
    @SerializedName("area")
    val area: String, // front, back, garage, interior, exterior
    
    @SerializedName("status")
    val status: String, // armed, disarmed, partial, triggered
    
    @SerializedName("timestamp")
    val timestamp: String,
    
    @SerializedName("activeDevices")
    val activeDevices: Int,
    
    @SerializedName("totalDevices")
    val totalDevices: Int,
    
    @SerializedName("lastActivity")
    val lastActivity: String,
    
    @SerializedName("lastAlert")
    val lastAlert: String?,
    
    @SerializedName("batteryStatus")
    val batteryStatus: Map<String, String>, // deviceId -> status
    
    @SerializedName("signalStatus")
    val signalStatus: Map<String, Int>, // deviceId -> signal strength
    
    // Local fields
    val isCached: Boolean = false,
    val cacheTimestamp: Long = System.currentTimeMillis()
)
