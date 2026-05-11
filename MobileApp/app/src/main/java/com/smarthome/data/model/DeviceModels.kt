package com.smarthome.data.model

import com.google.gson.annotations.SerializedName

data class DevicesResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("devices")
    val devices: List<Device>,
    
    @SerializedName("total")
    val total: Int,
    
    @SerializedName("message")
    val message: String?
)

data class DeviceResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("device")
    val device: Device,
    
    @SerializedName("message")
    val message: String
)

data class DeviceRequest(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("type")
    val type: String,
    
    @SerializedName("room")
    val room: String,
    
    @SerializedName("properties")
    val properties: DeviceProperties,
    
    @SerializedName("manufacturer")
    val manufacturer: String,
    
    @SerializedName("model")
    val model: String
)

data class UpdateDeviceStatusRequest(
    @SerializedName("status")
    val status: String,
    
    @SerializedName("properties")
    val properties: Map<String, Any> = emptyMap()
)

data class Device(
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
    val serialNumber: String?
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
