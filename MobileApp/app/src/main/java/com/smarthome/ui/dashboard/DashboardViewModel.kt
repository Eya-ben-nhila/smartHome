package com.smarthome.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smarthome.data.model.Device
import com.smarthome.data.repository.DeviceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    private val _devices = MutableStateFlow<List<Device>>(emptyList())
    val devices: StateFlow<List<Device>> = _devices.asStateFlow()

    private val _favoriteDevices = MutableStateFlow<List<Device>>(emptyList())
    val favoriteDevices: StateFlow<List<Device>> = _favoriteDevices.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _deviceStats = MutableStateFlow(DeviceStats())
    val deviceStats: StateFlow<DeviceStats> = _deviceStats.asStateFlow()

    init {
        loadDevices()
        loadFavoriteDevices()
    }

    fun loadDevices() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val result = deviceRepository.getDevices()
                result.fold(
                    onSuccess = { devices ->
                        _devices.value = devices
                        updateDeviceStats(devices)
                    },
                    onFailure = { error ->
                        _errorMessage.value = error.message ?: "Failed to load devices"
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to load devices"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadFavoriteDevices() {
        viewModelScope.launch {
            try {
                val result = deviceRepository.getFavoriteDevices()
                result.fold(
                    onSuccess = { devices ->
                        _favoriteDevices.value = devices
                    },
                    onFailure = { error ->
                        // Don't show error for favorites, just log it
                    }
                )
            } catch (e: Exception) {
                // Silently handle favorite devices error
            }
        }
    }

    fun refreshDevices() {
        viewModelScope.launch {
            _isRefreshing.value = true
            
            try {
                val result = deviceRepository.syncDevices()
                result.fold(
                    onSuccess = { devices ->
                        _devices.value = devices
                        updateDeviceStats(devices)
                        loadFavoriteDevices() // Refresh favorites too
                    },
                    onFailure = { error ->
                        _errorMessage.value = error.message ?: "Failed to refresh devices"
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to refresh devices"
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    fun updateDeviceStatus(deviceId: String, status: String) {
        viewModelScope.launch {
            try {
                val result = deviceRepository.updateDeviceStatus(deviceId, status)
                result.fold(
                    onSuccess = {
                        // Refresh devices to get updated status
                        loadDevices()
                    },
                    onFailure = { error ->
                        _errorMessage.value = error.message ?: "Failed to update device status"
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to update device status"
            }
        }
    }

    fun toggleFavorite(deviceId: String) {
        viewModelScope.launch {
            try {
                val result = deviceRepository.toggleFavorite(deviceId)
                result.fold(
                    onSuccess = {
                        // Refresh both lists
                        loadDevices()
                        loadFavoriteDevices()
                    },
                    onFailure = { error ->
                        _errorMessage.value = error.message ?: "Failed to toggle favorite"
                    }
                )
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to toggle favorite"
            }
        }
    }

    fun getDevicesByType(type: String): List<Device> {
        return _devices.value.filter { it.type.equals(type, ignoreCase = true) }
    }

    fun getDevicesByRoom(room: String): List<Device> {
        return _devices.value.filter { it.room.equals(room, ignoreCase = true) }
    }

    fun getOnlineDevices(): List<Device> {
        return _devices.value.filter { it.isOnline }
    }

    fun getOfflineDevices(): List<Device> {
        return _devices.value.filter { !it.isOnline }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    private fun updateDeviceStats(devices: List<Device>) {
        val stats = DeviceStats(
            totalDevices = devices.size,
            onlineDevices = devices.count { it.isOnline },
            offlineDevices = devices.count { !it.isOnline },
            devicesByType = devices.groupBy { it.type }.mapValues { it.value.size },
            devicesByRoom = devices.groupBy { it.room }.mapValues { it.value.size },
            favoriteDevices = devices.count { device ->
                _favoriteDevices.value.any { it.id == device.id }
            }
        )
        _deviceStats.value = stats
    }
}

data class DeviceStats(
    val totalDevices: Int = 0,
    val onlineDevices: Int = 0,
    val offlineDevices: Int = 0,
    val devicesByType: Map<String, Int> = emptyMap(),
    val devicesByRoom: Map<String, Int> = emptyMap(),
    val favoriteDevices: Int = 0
) {
    val onlinePercentage: Int
        get() = if (totalDevices > 0) (onlineDevices * 100) / totalDevices else 0

    val mostCommonType: String?
        get() = devicesByType.maxByOrNull { it.value }?.key

    val mostCommonRoom: String?
        get() = devicesByRoom.maxByOrNull { it.value }?.key
}
