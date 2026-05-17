package com.smarthome.data.api

import com.smarthome.data.api.model.*
import retrofit2.Response
import retrofit2.http.*

interface SmartHomeApiService {
    
    // Authentication
    @POST("api/auth/register")
    suspend fun register(@Body request: BackendRegisterRequest): Response<BackendAuthResponse>
    
    @POST("api/auth/login")
    suspend fun login(@Body request: BackendLoginRequest): Response<BackendAuthResponse>
    
    @GET("api/auth/health")
    suspend fun healthCheck(): Response<Map<String, String>>
    
    // Device Management
    @GET("api/devices")
    suspend fun getAllDevices(@Header("Authorization") token: String): Response<List<BackendDevice>>
    
    @GET("api/devices/{deviceId}")
    suspend fun getDeviceById(
        @Header("Authorization") token: String,
        @Path("deviceId") deviceId: String
    ): Response<BackendDevice>
    
    @POST("api/devices")
    suspend fun createDevice(
        @Header("Authorization") token: String,
        @Body request: BackendDeviceRequest
    ): Response<BackendDevice>
    
    @PUT("api/devices/{deviceId}")
    suspend fun updateDevice(
        @Header("Authorization") token: String,
        @Path("deviceId") deviceId: String,
        @Body request: BackendDeviceRequest
    ): Response<BackendDevice>
    
    @DELETE("api/devices/{deviceId}")
    suspend fun deleteDevice(
        @Header("Authorization") token: String,
        @Path("deviceId") deviceId: String
    ): Response<Unit>
    
    @PUT("api/devices/{deviceId}/status")
    suspend fun updateDeviceStatus(
        @Header("Authorization") token: String,
        @Path("deviceId") deviceId: String,
        @Body request: BackendDeviceStatusRequest
    ): Response<BackendDevice>
    
    @GET("api/devices/online")
    suspend fun getOnlineDevices(@Header("Authorization") token: String): Response<List<BackendDevice>>
    
    @GET("api/devices/type/{deviceType}")
    suspend fun getDevicesByType(
        @Header("Authorization") token: String,
        @Path("deviceType") deviceType: String
    ): Response<List<BackendDevice>>
    
    @POST("api/devices/{deviceId}/command")
    suspend fun sendDeviceCommand(
        @Header("Authorization") token: String,
        @Path("deviceId") deviceId: String,
        @Body request: BackendDeviceCommandRequest
    ): Response<Unit>
    
    @GET("api/devices/stats")
    suspend fun getDeviceStats(@Header("Authorization") token: String): Response<BackendDeviceStats>
    
    // Energy Management
    @GET("api/energy/dashboard")
    suspend fun getEnergyDashboard(@Header("Authorization") token: String): Response<Map<String, Any>>
    
    @GET("api/energy/readings/{deviceId}")
    suspend fun getEnergyReadings(
        @Header("Authorization") token: String,
        @Path("deviceId") deviceId: String
    ): Response<List<Map<String, Any>>>
    
    @POST("api/energy/readings")
    suspend fun createEnergyReading(
        @Header("Authorization") token: String,
        @Body request: Map<String, Any>
    ): Response<Map<String, Any>>
    
    // Automation Management
    @GET("api/automations")
    suspend fun getAllAutomations(@Header("Authorization") token: String): Response<List<Map<String, Any>>>
    
    @POST("api/automations")
    suspend fun createAutomation(
        @Header("Authorization") token: String,
        @Body request: Map<String, Any>
    ): Response<Map<String, Any>>
    
    @PUT("api/automations/{automationId}")
    suspend fun updateAutomation(
        @Header("Authorization") token: String,
        @Path("automationId") automationId: String,
        @Body request: Map<String, Any>
    ): Response<Map<String, Any>>
    
    @DELETE("api/automations/{automationId}")
    suspend fun deleteAutomation(
        @Header("Authorization") token: String,
        @Path("automationId") automationId: String
    ): Response<Unit>
    
    @PUT("api/automations/{automationId}/toggle")
    suspend fun toggleAutomation(
        @Header("Authorization") token: String,
        @Path("automationId") automationId: String
    ): Response<Map<String, Any>>
    
    @POST("api/automations/{automationId}/execute")
    suspend fun executeAutomation(
        @Header("Authorization") token: String,
        @Path("automationId") automationId: String
    ): Response<Unit>
    
    @GET("api/automations/stats")
    suspend fun getAutomationStats(@Header("Authorization") token: String): Response<Map<String, Any>>
    
    // Security/Alerts
    @GET("api/alerts")
    suspend fun getAlerts(@Header("Authorization") token: String): Response<List<Map<String, Any>>>
    
    @PUT("api/alerts/{alertId}/resolve")
    suspend fun resolveAlert(
        @Header("Authorization") token: String,
        @Path("alertId") alertId: String
    ): Response<Map<String, Any>>
}
