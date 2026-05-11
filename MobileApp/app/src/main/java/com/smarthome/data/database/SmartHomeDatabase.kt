package com.smarthome.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import com.smarthome.data.database.entities.UserEntity
import com.smarthome.data.database.entities.DeviceEntity
import com.smarthome.data.database.entities.EnergyEntity
import com.smarthome.data.database.entities.EnergySummaryEntity
import com.smarthome.data.database.entities.SecurityEventEntity
import com.smarthome.data.database.entities.SecurityStatusEntity
import com.smarthome.data.database.entities.AutomationEntity
import com.smarthome.data.database.converters.Converters
import javax.inject.Inject
import javax.inject.Singleton

@Database(
    entities = [
        UserEntity::class,
        DeviceEntity::class,
        EnergyEntity::class,
        EnergySummaryEntity::class,
        SecurityEventEntity::class,
        SecurityStatusEntity::class,
        AutomationEntity::class
    ],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
@Singleton
abstract class SmartHomeDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun deviceDao(): DeviceDao
    abstract fun energyDao(): EnergyDao
    abstract fun securityDao(): SecurityDao
    abstract fun automationDao(): AutomationDao

    companion object {
        const val DATABASE_NAME = "smarthome_database"
    }
}

// Database Migrations
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add new columns or tables as needed
    }
}

// DAO Interfaces
@androidx.room.Dao
interface UserDao {
    @androidx.room.Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUserById(id: String): UserEntity?

    @androidx.room.Query("SELECT * FROM users WHERE email = :email")
    suspend fun getUserByEmail(email: String): UserEntity?

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @androidx.room.Update
    suspend fun updateUser(user: UserEntity)

    @androidx.room.Delete
    suspend fun deleteUser(user: UserEntity)

    @androidx.room.Query("DELETE FROM users")
    suspend fun deleteAllUsers()
}

@androidx.room.Dao
interface DeviceDao {
    @androidx.room.Query("SELECT * FROM devices ORDER BY isFavorite DESC, name ASC")
    suspend fun getAllDevices(): List<DeviceEntity>

    @androidx.room.Query("SELECT * FROM devices WHERE id = :id")
    suspend fun getDeviceById(id: String): DeviceEntity?

    @androidx.room.Query("SELECT * FROM devices WHERE type = :type")
    suspend fun getDevicesByType(type: String): List<DeviceEntity>

    @androidx.room.Query("SELECT * FROM devices WHERE room = :room")
    suspend fun getDevicesByRoom(room: String): List<DeviceEntity>

    @androidx.room.Query("SELECT * FROM devices WHERE isOnline = :isOnline")
    suspend fun getDevicesByOnlineStatus(isOnline: Boolean): List<DeviceEntity>

    @androidx.room.Query("SELECT * FROM devices WHERE isFavorite = :isFavorite")
    suspend fun getFavoriteDevices(isFavorite: Boolean = true): List<DeviceEntity>

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: DeviceEntity)

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertDevices(devices: List<DeviceEntity>)

    @androidx.room.Update
    suspend fun updateDevice(device: DeviceEntity)

    @androidx.room.Delete
    suspend fun deleteDevice(device: DeviceEntity)

    @androidx.room.Query("DELETE FROM devices")
    suspend fun deleteAllDevices()

    @androidx.room.Query("UPDATE devices SET isFavorite = :isFavorite WHERE id = :deviceId")
    suspend fun updateFavoriteStatus(deviceId: String, isFavorite: Boolean)
}

@androidx.room.Dao
interface EnergyDao {
    @androidx.room.Query("SELECT * FROM energy_data WHERE deviceId = :deviceId ORDER BY timestamp DESC")
    suspend fun getEnergyDataByDevice(deviceId: String): List<EnergyEntity>

    @androidx.room.Query("SELECT * FROM energy_data WHERE type = :type ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getEnergyDataByType(type: String, limit: Int = 100): List<EnergyEntity>

    @androidx.room.Query("SELECT * FROM energy_summary WHERE period = :period")
    suspend fun getEnergySummary(period: String): EnergySummaryEntity?

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertEnergyData(energyData: EnergyEntity)

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertEnergyDataList(energyDataList: List<EnergyEntity>)

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertEnergySummary(summary: EnergySummaryEntity)

    @androidx.room.Delete
    suspend fun deleteEnergyData(energyData: EnergyEntity)

    @androidx.room.Query("DELETE FROM energy_data WHERE timestamp < :timestamp")
    suspend fun deleteOldEnergyData(timestamp: String)
}

@androidx.room.Dao
interface SecurityDao {
    @androidx.room.Query("SELECT * FROM security_events ORDER BY timestamp DESC LIMIT :limit")
    suspend fun getSecurityEvents(limit: Int = 50): List<SecurityEventEntity>

    @androidx.room.Query("SELECT * FROM security_events WHERE deviceId = :deviceId ORDER BY timestamp DESC")
    suspend fun getSecurityEventsByDevice(deviceId: String): List<SecurityEventEntity>

    @androidx.room.Query("SELECT * FROM security_events WHERE severity = :severity ORDER BY timestamp DESC")
    suspend fun getSecurityEventsBySeverity(severity: String): List<SecurityEventEntity>

    @androidx.room.Query("SELECT * FROM security_events WHERE isAcknowledged = :isAcknowledged ORDER BY timestamp DESC")
    suspend fun getUnacknowledgedEvents(isAcknowledged: Boolean = false): List<SecurityEventEntity>

    @androidx.room.Query("SELECT * FROM security_status")
    suspend fun getSecurityStatus(): List<SecurityStatusEntity>

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertSecurityEvent(event: SecurityEventEntity)

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertSecurityEvents(events: List<SecurityEventEntity>)

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertSecurityStatus(status: SecurityStatusEntity)

    @androidx.room.Update
    suspend fun updateSecurityEvent(event: SecurityEventEntity)

    @androidx.room.Query("UPDATE security_events SET isAcknowledged = :isAcknowledged, acknowledgedBy = :acknowledgedBy, acknowledgedAt = :acknowledgedAt WHERE id = :eventId")
    suspend fun acknowledgeEvent(eventId: String, isAcknowledged: Boolean, acknowledgedBy: String, acknowledgedAt: String)

    @androidx.room.Query("DELETE FROM security_events WHERE timestamp < :timestamp")
    suspend fun deleteOldSecurityEvents(timestamp: String)
}

@androidx.room.Dao
interface AutomationDao {
    @androidx.room.Query("SELECT * FROM automations ORDER BY name ASC")
    suspend fun getAllAutomations(): List<AutomationEntity>

    @androidx.room.Query("SELECT * FROM automations WHERE isActive = :isActive")
    suspend fun getActiveAutomations(isActive: Boolean = true): List<AutomationEntity>

    @androidx.room.Query("SELECT * FROM automations WHERE type = :type")
    suspend fun getAutomationsByType(type: String): List<AutomationEntity>

    @androidx.room.Query("SELECT * FROM automations WHERE id = :id")
    suspend fun getAutomationById(id: String): AutomationEntity?

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertAutomation(automation: AutomationEntity)

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertAutomations(automations: List<AutomationEntity>)

    @androidx.room.Update
    suspend fun updateAutomation(automation: AutomationEntity)

    @androidx.room.Delete
    suspend fun deleteAutomation(automation: AutomationEntity)

    @androidx.room.Query("UPDATE automations SET isActive = :isActive WHERE id = :automationId")
    suspend fun updateAutomationStatus(automationId: String, isActive: Boolean)

    @androidx.room.Query("UPDATE automations SET lastExecuted = :lastExecuted, executionCount = executionCount + 1 WHERE id = :automationId")
    suspend fun updateExecutionTime(automationId: String, lastExecuted: String)
}
