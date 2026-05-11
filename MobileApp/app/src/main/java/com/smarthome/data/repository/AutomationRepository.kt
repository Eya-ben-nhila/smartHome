package com.smarthome.data.repository

import com.smarthome.data.model.*
import retrofit2.Response

interface AutomationRepository {
    suspend fun getAutomations(): Result<List<Automation>>
    suspend fun createAutomation(automation: AutomationRequest): Result<Automation>
    suspend fun updateAutomation(automationId: String, automation: AutomationRequest): Result<Automation>
    suspend fun deleteAutomation(automationId: String): Result<Unit>
    suspend fun executeAutomation(automationId: String): Result<Unit>
    suspend fun getActiveAutomations(): Result<List<Automation>>
    suspend fun getAutomationsByType(type: String): Result<List<Automation>>
    suspend fun toggleAutomationStatus(automationId: String): Result<Unit>
    suspend fun getCachedAutomations(): Result<List<Automation>>
    suspend fun cacheAutomations(automations: List<Automation>)
}

class AutomationRepositoryImpl(
    private val apiService: ApiService,
    private val automationDao: com.smarthome.data.database.AutomationDao
) : AutomationRepository {

    override suspend fun getAutomations(): Result<List<Automation>> {
        return try {
            val response = apiService.getAutomations()
            
            if (response.isSuccessful && response.body() != null) {
                val automationsResponse = response.body()!!
                
                // Cache automations locally
                cacheAutomations(automationsResponse.automations)
                
                Result.success(automationsResponse.automations)
            } else {
                // Fallback to cached automations
                val cachedAutomations = getCachedAutomations()
                if (cachedAutomations.isSuccess && cachedAutomations.getOrNull()!!.isNotEmpty()) {
                    Result.success(cachedAutomations.getOrThrow())
                } else {
                    Result.success(getDemoAutomations())
                }
            }
        } catch (e: Exception) {
            // Network error, fallback to cached automations
            val cachedAutomations = getCachedAutomations()
            if (cachedAutomations.isSuccess && cachedAutomations.getOrNull()!!.isNotEmpty()) {
                Result.success(cachedAutomations.getOrThrow())
            } else {
                Result.success(getDemoAutomations())
            }
        }
    }

    override suspend fun createAutomation(automation: AutomationRequest): Result<Automation> {
        return try {
            val response = apiService.createAutomation(automation)
            
            if (response.isSuccessful && response.body() != null) {
                val automationResponse = response.body()!!
                
                // Cache new automation
                cacheAutomations(listOf(automationResponse.automation))
                
                Result.success(automationResponse.automation)
            } else {
                Result.failure(Exception("Failed to create automation: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateAutomation(automationId: String, automation: AutomationRequest): Result<Automation> {
        return try {
            val response = apiService.updateAutomation(automationId, automation)
            
            if (response.isSuccessful && response.body() != null) {
                val automationResponse = response.body()!!
                
                // Update cache
                cacheAutomations(listOf(automationResponse.automation))
                
                Result.success(automationResponse.automation)
            } else {
                Result.failure(Exception("Failed to update automation: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteAutomation(automationId: String): Result<Unit> {
        return try {
            val response = apiService.deleteAutomation(automationId)
            
            if (response.isSuccessful) {
                // Remove from cache
                val cachedAutomation = automationDao.getAutomationById(automationId)
                if (cachedAutomation != null) {
                    automationDao.deleteAutomation(cachedAutomation)
                }
                
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to delete automation: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun executeAutomation(automationId: String): Result<Unit> {
        return try {
            val response = apiService.executeAutomation(automationId)
            
            if (response.isSuccessful) {
                // Update execution time in cache
                automationDao.updateExecutionTime(automationId, java.time.Instant.now().toString())
                
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to execute automation: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getActiveAutomations(): Result<List<Automation>> {
        return try {
            val allAutomations = getAutomations().getOrNull() ?: return Result.failure(Exception("No automations available"))
            val activeAutomations = allAutomations.filter { it.isActive }
            Result.success(activeAutomations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAutomationsByType(type: String): Result<List<Automation>> {
        return try {
            val allAutomations = getAutomations().getOrNull() ?: return Result.failure(Exception("No automations available"))
            val filteredAutomations = allAutomations.filter { it.type.equals(type, ignoreCase = true) }
            Result.success(filteredAutomations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun toggleAutomationStatus(automationId: String): Result<Unit> {
        return try {
            val cachedAutomation = automationDao.getAutomationById(automationId)
            if (cachedAutomation != null) {
                val newStatus = !cachedAutomation.isActive
                automationDao.updateAutomationStatus(automationId, newStatus)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Automation not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCachedAutomations(): Result<List<Automation>> {
        return try {
            val cachedAutomations = automationDao.getAllAutomations()
            Result.success(cachedAutomations.map { it.toAutomation() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cacheAutomations(automations: List<Automation>) {
        try {
            val automationEntities = automations.map { automation ->
                com.smarthome.data.database.entities.AutomationEntity(
                    id = automation.id,
                    name = automation.name,
                    description = automation.description,
                    type = automation.type,
                    triggerType = automation.triggerType,
                    isActive = automation.isActive,
                    icon = automation.icon,
                    conditions = automation.conditions,
                    actions = automation.actions,
                    schedule = automation.schedule,
                    lastExecuted = automation.lastExecuted,
                    nextExecution = automation.nextExecution,
                    executionCount = automation.executionCount,
                    successCount = automation.successCount,
                    failureCount = automation.failureCount,
                    createdBy = "user",
                    createdAt = automation.createdAt,
                    updatedAt = automation.updatedAt,
                    isCached = true,
                    cacheTimestamp = System.currentTimeMillis()
                )
            }
            
            automationDao.insertAutomations(automationEntities)
        } catch (e: Exception) {
            // Handle cache error silently
        }
    }

    private fun getDemoAutomations(): List<Automation> {
        return listOf(
            Automation(
                id = "automation_morning_routine",
                name = "Morning Routine",
                description = "Turn on lights and start coffee maker at 7 AM",
                type = "routine",
                triggerType = "time",
                isActive = true,
                icon = "sunrise",
                conditions = emptyList(),
                actions = listOf(
                    AutomationAction(
                        deviceId = "light_living_room",
                        deviceType = "light",
                        action = "turn_on",
                        parameters = mapOf("brightness" to 70),
                        delay = 0
                    ),
                    AutomationAction(
                        deviceId = "coffee_maker",
                        deviceType = "appliance",
                        action = "turn_on",
                        parameters = emptyMap(),
                        delay = 300
                    )
                ),
                schedule = AutomationSchedule(
                    type = "daily",
                    time = "07:00",
                    days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday"),
                    date = null,
                    interval = null,
                    timezone = "UTC"
                ),
                lastExecuted = "2024-01-15T07:00:00Z",
                nextExecution = "2024-01-16T07:00:00Z",
                executionCount = 45,
                successCount = 43,
                failureCount = 2,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-10T15:30:00Z"
            ),
            Automation(
                id = "automation_security_night",
                name = "Night Security Mode",
                description = "Arm security system and turn off all lights at 11 PM",
                type = "security",
                triggerType = "time",
                isActive = true,
                icon = "shield",
                conditions = emptyList(),
                actions = listOf(
                    AutomationAction(
                        deviceId = "security_system",
                        deviceType = "security",
                        action = "arm",
                        parameters = mapOf("mode" to "night"),
                        delay = 0
                    ),
                    AutomationAction(
                        deviceId = "all_lights",
                        deviceType = "light",
                        action = "turn_off",
                        parameters = emptyMap(),
                        delay = 0
                    )
                ),
                schedule = AutomationSchedule(
                    type = "daily",
                    time = "23:00",
                    days = listOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"),
                    date = null,
                    interval = null,
                    timezone = "UTC"
                ),
                lastExecuted = "2024-01-14T23:00:00Z",
                nextExecution = "2024-01-15T23:00:00Z",
                executionCount = 30,
                successCount = 30,
                failureCount = 0,
                createdAt = "2024-01-01T00:00:00Z",
                updatedAt = "2024-01-05T10:15:00Z"
            )
        )
    }
}

// Extension function for mapping
private fun com.smarthome.data.database.entities.AutomationEntity.toAutomation(): Automation {
    return Automation(
        id = this.id,
        name = this.name,
        description = this.description,
        type = this.type,
        triggerType = this.triggerType,
        isActive = this.isActive,
        icon = this.icon,
        conditions = this.conditions,
        actions = this.actions,
        schedule = this.schedule,
        lastExecuted = this.lastExecuted,
        nextExecution = this.nextExecution,
        executionCount = this.executionCount,
        successCount = this.successCount,
        failureCount = this.failureCount,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
