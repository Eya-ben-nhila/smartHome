package com.smarthome.data.model

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String
)

data class EnergyResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("data")
    val data: EnergyData,
    
    @SerializedName("summary")
    val summary: EnergySummary
)

data class EnergyData(
    @SerializedName("consumption")
    val consumption: List<EnergyConsumption>,
    
    @SerializedName("cost")
    val cost: List<EnergyCost>,
    
    @SerializedName("efficiency")
    val efficiency: Double,
    
    @SerializedName("savings")
    val savings: Double
)

data class EnergyConsumption(
    @SerializedName("deviceId")
    val deviceId: String,
    
    @SerializedName("deviceName")
    val deviceName: String,
    
    @SerializedName("timestamp")
    val timestamp: String,
    
    @SerializedName("consumption")
    val consumption: Double,
    
    @SerializedName("cost")
    val cost: Double,
    
    @SerializedName("peakHours")
    val peakHours: Boolean
)

data class EnergyCost(
    @SerializedName("period")
    val period: String,
    
    @SerializedName("amount")
    val amount: Double,
    
    @SerializedName("currency")
    val currency: String
)

data class EnergySummary(
    @SerializedName("totalConsumption")
    val totalConsumption: Double,
    
    @SerializedName("totalCost")
    val totalCost: Double,
    
    @SerializedName("averageDaily")
    val averageDaily: Double,
    
    @SerializedName("peakConsumption")
    val peakConsumption: Double,
    
    @SerializedName("renewablePercentage")
    val renewablePercentage: Double,
    
    @SerializedName("carbonFootprint")
    val carbonFootprint: Double,
    
    @SerializedName("efficiencyScore")
    val efficiencyScore: Double
)

data class SecurityResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("events")
    val events: List<SecurityEvent>,
    
    @SerializedName("status")
    val status: List<SecurityStatus>
)

data class SecurityEvent(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("deviceId")
    val deviceId: String?,
    
    @SerializedName("deviceName")
    val deviceName: String?,
    
    @SerializedName("type")
    val type: String,
    
    @SerializedName("event")
    val event: String,
    
    @SerializedName("severity")
    val severity: String,
    
    @SerializedName("timestamp")
    val timestamp: String,
    
    @SerializedName("location")
    val location: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("isAcknowledged")
    val isAcknowledged: Boolean,
    
    @SerializedName("imageUrl")
    val imageUrl: String?,
    
    @SerializedName("videoUrl")
    val videoUrl: String?
)

data class SecurityStatus(
    @SerializedName("area")
    val area: String,
    
    @SerializedName("status")
    val status: String,
    
    @SerializedName("activeDevices")
    val activeDevices: Int,
    
    @SerializedName("totalDevices")
    val totalDevices: Int,
    
    @SerializedName("lastActivity")
    val lastActivity: String
)

data class AutomationsResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("automations")
    val automations: List<Automation>,
    
    @SerializedName("total")
    val total: Int
)

data class AutomationRequest(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("type")
    val type: String,
    
    @SerializedName("triggerType")
    val triggerType: String,
    
    @SerializedName("conditions")
    val conditions: List<AutomationCondition>,
    
    @SerializedName("actions")
    val actions: List<AutomationAction>,
    
    @SerializedName("schedule")
    val schedule: AutomationSchedule?
)

data class AutomationResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("automation")
    val automation: Automation,
    
    @SerializedName("message")
    val message: String
)

data class Automation(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("type")
    val type: String,
    
    @SerializedName("triggerType")
    val triggerType: String,
    
    @SerializedName("isActive")
    val isActive: Boolean,
    
    @SerializedName("icon")
    val icon: String,
    
    @SerializedName("conditions")
    val conditions: List<AutomationCondition>,
    
    @SerializedName("actions")
    val actions: List<AutomationAction>,
    
    @SerializedName("schedule")
    val schedule: AutomationSchedule?,
    
    @SerializedName("lastExecuted")
    val lastExecuted: String?,
    
    @SerializedName("nextExecution")
    val nextExecution: String?,
    
    @SerializedName("executionCount")
    val executionCount: Int,
    
    @SerializedName("successCount")
    val successCount: Int,
    
    @SerializedName("failureCount")
    val failureCount: Int,
    
    @SerializedName("createdAt")
    val createdAt: String,
    
    @SerializedName("updatedAt")
    val updatedAt: String
)

data class AutomationCondition(
    @SerializedName("deviceId")
    val deviceId: String?,
    
    @SerializedName("deviceType")
    val deviceType: String?,
    
    @SerializedName("property")
    val property: String,
    
    @SerializedName("operator")
    val operator: String,
    
    @SerializedName("value")
    val value: Any,
    
    @SerializedName("logicalOperator")
    val logicalOperator: String?
)

data class AutomationAction(
    @SerializedName("deviceId")
    val deviceId: String?,
    
    @SerializedName("deviceType")
    val deviceType: String?,
    
    @SerializedName("action")
    val action: String,
    
    @SerializedName("parameters")
    val parameters: Map<String, Any>,
    
    @SerializedName("delay")
    val delay: Int
)

data class AutomationSchedule(
    @SerializedName("type")
    val type: String,
    
    @SerializedName("time")
    val time: String,
    
    @SerializedName("days")
    val days: List<String>?,
    
    @SerializedName("date")
    val date: String?,
    
    @SerializedName("interval")
    val interval: Int?,
    
    @SerializedName("timezone")
    val timezone: String
)

data class ProfileResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("user")
    val user: User,
    
    @SerializedName("message")
    val message: String
)

data class AlertsResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("alerts")
    val alerts: List<Alert>,
    
    @SerializedName("unreadCount")
    val unreadCount: Int
)

data class Alert(
    @SerializedName("id")
    val id: String,
    
    @SerializedName("type")
    val type: String,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("severity")
    val severity: String,
    
    @SerializedName("timestamp")
    val timestamp: String,
    
    @SerializedName("isRead")
    val isRead: Boolean,
    
    @SerializedName("deviceId")
    val deviceId: String?,
    
    @SerializedName("actionUrl")
    val actionUrl: String?
)

data class HealthResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("status")
    val status: String,
    
    @SerializedName("timestamp")
    val timestamp: String,
    
    @SerializedName("version")
    val version: String,
    
    @SerializedName("uptime")
    val uptime: Long
)
