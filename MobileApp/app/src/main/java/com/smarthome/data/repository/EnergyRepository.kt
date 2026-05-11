package com.smarthome.data.repository

import com.smarthome.data.model.*
import retrofit2.Response

interface EnergyRepository {
    suspend fun getEnergyData(): Result<EnergyResponse>
    suspend fun getEnergyConsumption(period: String): Result<List<EnergyConsumption>>
    suspend fun getEnergyCost(period: String): Result<EnergyCost>
    suspend fun getEnergySummary(period: String): Result<EnergySummary>
    suspend fun getCachedEnergyData(): Result<EnergyData>
    suspend fun cacheEnergyData(data: EnergyData)
}

class EnergyRepositoryImpl(
    private val apiService: ApiService,
    private val energyDao: com.smarthome.data.database.EnergyDao
) : EnergyRepository {

    override suspend fun getEnergyData(): Result<EnergyResponse> {
        return try {
            val response = apiService.getEnergyData()
            
            if (response.isSuccessful && response.body() != null) {
                val energyResponse = response.body()!!
                
                // Cache energy data locally
                cacheEnergyData(energyResponse.data)
                
                Result.success(energyResponse)
            } else {
                // Fallback to cached data
                val cachedData = getCachedEnergyData()
                if (cachedData.isSuccess) {
                    val mockResponse = EnergyResponse(
                        success = true,
                        data = cachedData.getOrThrow(),
                        summary = getDemoSummary()
                    )
                    Result.success(mockResponse)
                } else {
                    Result.failure(Exception("Failed to load energy data"))
                }
            }
        } catch (e: Exception) {
            // Network error, fallback to cached data
            val cachedData = getCachedEnergyData()
            if (cachedData.isSuccess) {
                val mockResponse = EnergyResponse(
                    success = true,
                    data = cachedData.getOrThrow(),
                    summary = getDemoSummary()
                )
                Result.success(mockResponse)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun getEnergyConsumption(period: String): Result<List<EnergyConsumption>> {
        return try {
            val response = apiService.getEnergyData()
            
            if (response.isSuccessful && response.body() != null) {
                val consumptionData = response.body()!!.data.consumption
                Result.success(consumptionData)
            } else {
                Result.failure(Exception("Failed to load consumption data"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEnergyCost(period: String): Result<EnergyCost> {
        return try {
            val response = apiService.getEnergyData()
            
            if (response.isSuccessful && response.body() != null) {
                val costData = response.body()!!.data.cost.firstOrNull()
                if (costData != null) {
                    Result.success(costData)
                } else {
                    Result.failure(Exception("No cost data available"))
                }
            } else {
                Result.failure(Exception("Failed to load cost data"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getEnergySummary(period: String): Result<EnergySummary> {
        return try {
            val response = apiService.getEnergyData()
            
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!.summary)
            } else {
                Result.failure(Exception("Failed to load energy summary"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCachedEnergyData(): Result<EnergyData> {
        return try {
            val cachedConsumption = energyDao.getEnergyDataByType("hourly", 100)
            val energyData = EnergyData(
                consumption = cachedConsumption.map { entity ->
                    EnergyConsumption(
                        deviceId = entity.deviceId,
                        deviceName = "Device ${entity.deviceId}",
                        timestamp = entity.timestamp,
                        consumption = entity.consumption,
                        cost = entity.cost,
                        peakHours = entity.peakHours
                    )
                },
                cost = getDemoCostData(),
                efficiency = 85.5,
                savings = 125.50
            )
            Result.success(energyData)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cacheEnergyData(data: EnergyData) {
        try {
            val energyEntities = data.consumption.map { consumption ->
                com.smarthome.data.database.entities.EnergyEntity(
                    id = "${consumption.deviceId}_${consumption.timestamp}",
                    deviceId = consumption.deviceId,
                    timestamp = consumption.timestamp,
                    type = "hourly",
                    consumption = consumption.consumption,
                    cost = consumption.cost,
                    peakHours = consumption.peakHours,
                    renewable = 0.0,
                    grid = consumption.consumption,
                    battery = 0.0,
                    carbonFootprint = consumption.consumption * 0.5, // Example calculation
                    efficiency = 85.5,
                    savings = consumption.cost * 0.15, // 15% savings estimate
                    isCached = true,
                    cacheTimestamp = System.currentTimeMillis()
                )
            }
            
            energyDao.insertEnergyDataList(energyEntities)
        } catch (e: Exception) {
            // Handle cache error silently
        }
    }

    private fun getDemoSummary(): EnergySummary {
        return EnergySummary(
            totalConsumption = 1250.5,
            totalCost = 245.75,
            averageDaily = 41.68,
            peakConsumption = 89.2,
            renewablePercentage = 25.5,
            carbonFootprint = 625.25,
            efficiencyScore = 85.5,
            totalSavings = 125.50
        )
    }

    private fun getDemoCostData(): List<EnergyCost> {
        return listOf(
            EnergyCost(
                period = "daily",
                amount = 8.25,
                currency = "USD"
            ),
            EnergyCost(
                period = "weekly",
                amount = 57.75,
                currency = "USD"
            ),
            EnergyCost(
                period = "monthly",
                amount = 245.75,
                currency = "USD"
            )
        )
    }
}
