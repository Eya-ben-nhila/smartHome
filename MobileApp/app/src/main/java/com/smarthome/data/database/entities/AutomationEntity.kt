package com.smarthome.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "automations",
    indices = [
        Index(value = ["isActive"]),
        Index(value = ["type"]),
        Index(value = ["triggerType"])
    ]
)
data class AutomationEntity(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("description")
    val description: String,
    
    @SerializedName("type")
    val type: String, // schedule, trigger, scene, routine
    
    @SerializedName("triggerType")
    val triggerType: String, // time, device, sensor, location, weather
    
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
    
    @SerializedName("createdBy")
    val createdBy: String,
    
    @SerializedName("createdAt")
    val createdAt: String,
    
    @SerializedName("updatedAt")
    val updatedAt: String,
    
    // Local fields
    val isCached: Boolean = false,
    val cacheTimestamp: Long = System.currentTimeMillis()
)

data class AutomationCondition(
    @SerializedName("deviceId")
    val deviceId: String?,
    
    @SerializedName("deviceType")
    val deviceType: String?,
    
    @SerializedName("property")
    val property: String, // status, temperature, humidity, brightness, etc.
    
    @SerializedName("operator")
    val operator: String, // equals, not_equals, greater_than, less_than, contains
    
    @SerializedName("value")
    val value: Any,
    
    @SerializedName("logicalOperator")
    val logicalOperator: String? // AND, OR for multiple conditions
)

data class AutomationAction(
    @SerializedName("deviceId")
    val deviceId: String?,
    
    @SerializedName("deviceType")
    val deviceType: String?,
    
    @SerializedName("action")
    val action: String, // turn_on, turn_off, set_temperature, set_brightness, etc.
    
    @SerializedName("parameters")
    val parameters: Map<String, Any> = emptyMap(),
    
    @SerializedName("delay")
    val delay: Int = 0 // seconds to wait before executing
)

data class AutomationSchedule(
    @SerializedName("type")
    val type: String, // daily, weekly, monthly, custom
    
    @SerializedName("time")
    val time: String, // HH:mm format
    
    @SerializedName("days")
    val days: List<String>? = emptyList(), // Monday, Tuesday, etc.
    
    @SerializedName("date")
    val date: String?, // for specific dates
    
    @SerializedName("interval")
    val interval: Int?, // for recurring intervals in minutes
    
    @SerializedName("timezone")
    val timezone: String = "UTC"
)
