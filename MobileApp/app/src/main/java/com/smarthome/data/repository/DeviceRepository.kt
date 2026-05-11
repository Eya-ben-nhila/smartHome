package com.smarthome.data.repository

import com.smarthome.data.model.*
import com.smarthome.data.database.entities.DeviceEntity
import retrofit2.Response

interface DeviceRepository {
    suspend fun getDevices(): Result<List<Device>>
    suspend fun getDeviceById(deviceId: String): Result<Device>
    suspend fun createDevice(device: DeviceRequest): Result<Device>
    suspend fun updateDevice(deviceId: String, device: DeviceRequest): Result<Device>
    suspend fun deleteDevice(deviceId: String): Result<Unit>
    suspend fun updateDeviceStatus(deviceId: String, status: String): Result<Unit>
    suspend fun getDevicesByType(type: String): Result<List<Device>>
    suspend fun getDevicesByRoom(room: String): Result<List<Device>>
    suspend fun getFavoriteDevices(): Result<List<Device>>
    suspend fun toggleFavorite(deviceId: String): Result<Unit>
    suspend fun syncDevices(): Result<List<Device>>
}

class DeviceRepositoryImpl(
    private val apiService: ApiService,
    private val deviceDao: DeviceDao
) : DeviceRepository {

    override suspend fun getDevices(): Result<List<Device>> {
        return try {
            val response = apiService.getDevices()
            
            if (response.isSuccessful && response.body() != null) {
                val devicesResponse = response.body()!!
                
                // Cache devices locally
                cacheDevices(devicesResponse.devices)
                
                Result.success(devicesResponse.devices)
            } else {
                // Fallback to cached devices
                val cachedDevices = getCachedDevices()
                Result.success(cachedDevices)
            }
        } catch (e: Exception) {
            // Network error, fallback to cached devices
            val cachedDevices = getCachedDevices()
            if (cachedDevices.isNotEmpty()) {
                Result.success(cachedDevices)
            } else {
                // Return demo devices if no cache available
                Result.success(getDemoDevices())
            }
        }
    }

    override suspend fun getDeviceById(deviceId: String): Result<Device> {
        return try {
            // Try cache first
            val cachedDevice = deviceDao.getDeviceById(deviceId)
            if (cachedDevice != null) {
                Result.success(cachedDevice.toDevice())
            } else {
                // Try API
                val response = apiService.getDevices()
                if (response.isSuccessful && response.body() != null) {
                    val device = response.body()!!.devices.find { it.id == deviceId }
                    if (device != null) {
                        Result.success(device)
                    } else {
                        Result.failure(Exception("Device not found"))
                    }
                } else {
                    Result.failure(Exception("Failed to fetch device"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createDevice(device: DeviceRequest): Result<Device> {
        return try {
            val response = apiService.createDevice(device)
            
            if (response.isSuccessful && response.body() != null) {
                val deviceResponse = response.body()!!
                
                // Cache new device
                cacheDevice(deviceResponse.device)
                
                Result.success(deviceResponse.device)
            } else {
                Result.failure(Exception("Failed to create device: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateDevice(deviceId: String, device: DeviceRequest): Result<Device> {
        return try {
            val response = apiService.updateDevice(deviceId, device)
            
            if (response.isSuccessful && response.body() != null) {
                val deviceResponse = response.body()!!
                
                // Update cache
                cacheDevice(deviceResponse.device)
                
                Result.success(deviceResponse.device)
            } else {
                Result.failure(Exception("Failed to update device: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteDevice(deviceId: String): Result<Unit> {
        return try {
            val response = apiService.deleteDevice(deviceId)
            
            if (response.isSuccessful) {
                // Remove from cache
                deviceDao.getDeviceById(deviceId)?.let { device ->
                    deviceDao.deleteDevice(device)
                }
                
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete device: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateDeviceStatus(deviceId: String, status: String): Result<Unit> {
        return try {
            val response = apiService.updateDeviceStatus(
                deviceId = deviceId,
                request = UpdateDeviceStatusRequest(status = status)
            )
            
            if (response.isSuccessful) {
                // Update cache
                deviceDao.getDeviceById(deviceId)?.let { device ->
                    val updatedDevice = device.copy(status = status)
                    deviceDao.updateDevice(updatedDevice)
                }
                
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to update device status: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDevicesByType(type: String): Result<List<Device>> {
        return try {
            val allDevices = getDevices().getOrNull() ?: return Result.failure(Exception("No devices available"))
            val filteredDevices = allDevices.filter { it.type.equals(type, ignoreCase = true) }
            Result.success(filteredDevices)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getDevicesByRoom(room: String): Result<List<Device>> {
        return try {
            val allDevices = getDevices().getOrNull() ?: return Result.failure(Exception("No devices available"))
            val filteredDevices = allDevices.filter { it.room.equals(room, ignoreCase = true) }
            Result.success(filteredDevices)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getFavoriteDevices(): Result<List<Device>> {
        return try {
            val favoriteDevices = deviceDao.getFavoriteDevices(true)
            Result.success(favoriteDevices.map { it.toDevice() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleFavorite(deviceId: String): Result<Unit> {
        return try {
            val device = deviceDao.getDeviceById(deviceId)
            if (device != null) {
                val newFavoriteStatus = !device.isFavorite
                deviceDao.updateFavoriteStatus(deviceId, newFavoriteStatus)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Device not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun syncDevices(): Result<List<Device>> {
        return try {
            val response = apiService.getDevices()
            
            if (response.isSuccessful && response.body() != null) {
                val devicesResponse = response.body()!!
                
                // Clear cache and insert fresh data
                deviceDao.deleteAllDevices()
                cacheDevices(devicesResponse.devices)
                
                Result.success(devicesResponse.devices)
            } else {
                Result.failure(Exception("Failed to sync devices"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun cacheDevices(devices: List<Device>) {
        devices.forEach { device ->
            cacheDevice(device)
        }
    }

    private suspend fun cacheDevice(device: Device) {
        val deviceEntity = device.toDeviceEntity()
        deviceDao.insertDevice(deviceEntity)
    }

    private suspend fun getCachedDevices(): List<Device> {
        return try {
            deviceDao.getAllDevices().map { it.toDevice() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun getDemoDevices(): List<Device> {
        return listOf(
            Device(
                id = "demo_light_1",
                name = "Living Room Light",
                type = "light",
                room = "Living Room",
                status = "on",
                isOnline = true,
                icon = "lightbulb",
                properties = DeviceProperties(
                    brightness = 75,
                    color = "#FFFFFF"
                ),
                lastSeen = "2024-01-15T10:30:00Z",
                batteryLevel = null,
                signalStrength = 85,
                firmware = "1.2.3",
                manufacturer = "SmartHome Inc",
                model = "SH-LIGHT-100",
                serialNumber = "SH100123456"
            ),
            Device(
                id = "demo_thermostat_1",
                name = "Main Thermostat",
                type = "thermostat",
                room = "Hallway",
                status = "active",
                isOnline = true,
                icon = "thermostat",
                properties = DeviceProperties(
                    temperature = 22.5,
                    mode = "auto"
                ),
                lastSeen = "2024-01-15T10:25:00Z",
                batteryLevel = null,
                signalStrength = 92,
                firmware = "2.1.0",
                manufacturer = "ClimateControl",
                model = "CC-THERM-200",
                serialNumber = "CC200789012"
            )
        )
    }
}

// Extension functions for mapping between entities and models
private fun DeviceEntity.toDevice(): Device {
    return Device(
        id = this.id,
        name = this.name,
        type = this.type,
        room = this.room,
        status = this.status,
        isOnline = this.isOnline,
        icon = this.icon,
        properties = this.properties,
        lastSeen = this.lastSeen,
        batteryLevel = this.batteryLevel,
        signalStrength = this.signalStrength,
        firmware = this.firmware,
        manufacturer = this.manufacturer,
        model = this.model,
        serialNumber = this.serialNumber
    )
}

private fun Device.toDeviceEntity(): DeviceEntity {
    return DeviceEntity(
        id = this.id,
        name = this.name,
        type = this.type,
        room = this.room,
        status = this.status,
        isOnline = this.isOnline,
        icon = this.icon,
        properties = this.properties,
        lastSeen = this.lastSeen,
        batteryLevel = this.batteryLevel,
        signalStrength = this.signalStrength,
        firmware = this.firmware,
        manufacturer = this.manufacturer,
        model = this.model,
        serialNumber = this.serialNumber,
        isCached = true,
        cacheTimestamp = System.currentTimeMillis(),
        isFavorite = false
    )
}
