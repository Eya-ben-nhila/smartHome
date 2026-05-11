package com.smarthome.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "devices",
    indices = [
        Index(value = ["type"]),
        Index(value = ["room"]),
        Index(value = ["isOnline"])
    ]
)
data class DeviceEntity(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("type")
    val type: String,
    
    @SerializedName("room")
    val room: String,
    
    @SerializedName("status")
    val status: String,
    
    @SerializedName("isOnline")
    val isOnline: Boolean,
    
    @SerializedName("icon")
    val icon: String,
    
    @SerializedName("properties")
    val properties: DeviceProperties,
    
    @SerializedName("lastSeen")
    val lastSeen: String,
    
    @SerializedName("batteryLevel")
    val batteryLevel: Int?,
    
    @SerializedName("signalStrength")
    val signalStrength: Int?,
    
    @SerializedName("firmware")
    val firmware: String?,
    
    @SerializedName("manufacturer")
    val manufacturer: String,
    
    @SerializedName("model")
    val model: String,
    
    @SerializedName("serialNumber")
    val serialNumber: String?,
    
    // Local fields
    val isCached: Boolean = false,
    val cacheTimestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)

data class DeviceProperties(
    @SerializedName("brightness")
    val brightness: Int?,
    
    @SerializedName("temperature")
    val temperature: Double?,
    
    @SerializedName("humidity")
    val humidity: Double?,
    
    @SerializedName("power")
    val power: Double?,
    
    @SerializedName("voltage")
    val voltage: Double?,
    
    @SerializedName("current")
    val current: Double?,
    
    @SerializedName("color")
    val color: String?,
    
    @SerializedName("volume")
    val volume: Int?,
    
    @SerializedName("channel")
    val channel: Int?,
    
    @SerializedName("speed")
    val speed: Int?,
    
    @SerializedName("mode")
    val mode: String?,
    
    @SerializedName("customProperties")
    val customProperties: Map<String, Any> = emptyMap()
)
