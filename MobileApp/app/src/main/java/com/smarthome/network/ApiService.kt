package com.smarthome.network

import retrofit2.Response
import retrofit2.http.*
import com.smarthome.data.model.*

interface ApiService {
    
    @POST("mobile/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    
    @GET("mobile/devices")
    suspend fun getDevices(): Response<DevicesResponse>
    
    @PUT("mobile/devices/{deviceId}/status")
    suspend fun updateDeviceStatus(
        @Path("deviceId") deviceId: String,
        @Body request: UpdateDeviceStatusRequest
    ): Response<BaseResponse>
    
    @GET("mobile/energy")
    suspend fun getEnergyData(): Response<EnergyResponse>
    
    @GET("mobile/security")
    suspend fun getSecurityEvents(): Response<SecurityResponse>
    
    @GET("mobile/automations")
    suspend fun getAutomations(): Response<AutomationsResponse>
    
    @GET("mobile/profile")
    suspend fun getProfile(): Response<ProfileResponse>
    
    @GET("mobile/alerts")
    suspend fun getAlerts(): Response<AlertsResponse>
    
    @GET("mobile/health")
    suspend fun healthCheck(): Response<HealthResponse>
    
    @POST("mobile/devices")
    suspend fun createDevice(@Body device: DeviceRequest): Response<DeviceResponse>
    
    @PUT("mobile/devices/{deviceId}")
    suspend fun updateDevice(
        @Path("deviceId") deviceId: String,
        @Body device: DeviceRequest
    ): Response<DeviceResponse>
    
    @DELETE("mobile/devices/{deviceId}")
    suspend fun deleteDevice(@Path("deviceId") deviceId: String): Response<BaseResponse>
    
    @POST("mobile/automations")
    suspend fun createAutomation(@Body automation: AutomationRequest): Response<AutomationResponse>
    
    @PUT("mobile/automations/{automationId}")
    suspend fun updateAutomation(
        @Path("automationId") automationId: String,
        @Body automation: AutomationRequest
    ): Response<AutomationResponse>
    
    @DELETE("mobile/automations/{automationId}")
    suspend fun deleteAutomation(@Path("automationId") automationId: String): Response<BaseResponse>
    
    @POST("mobile/automations/{automationId}/execute")
    suspend fun executeAutomation(@Path("automationId") automationId: String): Response<BaseResponse>
}
