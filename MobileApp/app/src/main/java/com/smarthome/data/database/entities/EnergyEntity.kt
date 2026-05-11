package com.smarthome.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = "energy_data",
    indices = [
        Index(value = ["deviceId"]),
        Index(value = ["timestamp"]),
        Index(value = ["type"])
    ]
)
data class EnergyEntity(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    
    @SerializedName("deviceId")
    val deviceId: String,
    
    @SerializedName("timestamp")
    val timestamp: String,
    
    @SerializedName("type")
    val type: String, // hourly, daily, weekly, monthly
    
    @SerializedName("consumption")
    val consumption: Double,
    
    @SerializedName("cost")
    val cost: Double,
    
    @SerializedName("peakHours")
    val peakHours: Boolean,
    
    @SerializedName("renewable")
    val renewable: Double,
    
    @SerializedName("grid")
    val grid: Double,
    
    @SerializedName("battery")
    val battery: Double,
    
    @SerializedName("carbonFootprint")
    val carbonFootprint: Double,
    
    @SerializedName("efficiency")
    val efficiency: Double,
    
    @SerializedName("savings")
    val savings: Double,
    
    // Local fields
    val isCached: Boolean = false,
    val cacheTimestamp: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "energy_summary",
    indices = [
        Index(value = ["period"]),
        Index(value = ["timestamp"])
    ]
)
data class EnergySummaryEntity(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    
    @SerializedName("period")
    val period: String, // today, week, month, year
    
    @SerializedName("timestamp")
    val timestamp: String,
    
    @SerializedName("totalConsumption")
    val totalConsumption: Double,
    
    @SerializedName("totalCost")
    val totalCost: Double,
    
    @SerializedName("averageDaily")
    val averageDaily: Double,
    
    @SerializedName("peakConsumption")
    val peakConsumption: Double,
    
    @SerializedName("offPeakConsumption")
    val offPeakConsumption: Double,
    
    @SerializedName("renewablePercentage")
    val renewablePercentage: Double,
    
    @SerializedName("carbonFootprint")
    val carbonFootprint: Double,
    
    @SerializedName("totalSavings")
    val totalSavings: Double,
    
    @SerializedName("efficiencyScore")
    val efficiencyScore: Double,
    
    @SerializedName("topDevices")
    val topDevices: List<TopDeviceEnergy>,
    
    @SerializedName("recommendations")
    val recommendations: List<String>,
    
    // Local fields
    val isCached: Boolean = false,
    val cacheTimestamp: Long = System.currentTimeMillis()
)

data class TopDeviceEnergy(
    @SerializedName("deviceId")
    val deviceId: String,
    
    @SerializedName("deviceName")
    val deviceName: String,
    
    @SerializedName("consumption")
    val consumption: Double,
    
    @SerializedName("percentage")
    val percentage: Double
)
