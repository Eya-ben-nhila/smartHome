package com.smarthome.data.api.model

data class BackendDevice(
    val id: String,
    val deviceName: String,
    val deviceType: String,
    val deviceStatus: String,
    val location: String,
    val manufacturer: String?,
    val model: String?,
    val firmwareVersion: String?,
    val isOnline: Boolean,
    val lastSeen: String?,
    val createdAt: String?
)

data class BackendDeviceRequest(
    val deviceName: String,
    val deviceType: String,
    val location: String,
    val manufacturer: String? = null,
    val model: String? = null,
    val firmwareVersion: String? = null,
    val macAddress: String? = null,
    val ipAddress: String? = null
)

data class BackendDeviceStatusRequest(
    val status: String
)

data class BackendDeviceCommandRequest(
    val command: String,
    val data: Map<String, Any>? = null
)

data class BackendDeviceStats(
    val totalDevices: Int,
    val onlineDevices: Int,
    val offlineDevices: Int
)
