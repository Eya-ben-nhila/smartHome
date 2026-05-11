package com.smarthome.data.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smarthome.data.database.entities.*

class Converters {
    private val gson = Gson()

    // UserPreferences converter
    @TypeConverter
    fun fromUserPreferences(preferences: UserPreferences): String {
        return gson.toJson(preferences)
    }

    @TypeConverter
    fun toUserPreferences(preferencesString: String): UserPreferences {
        return gson.fromJson(preferencesString, UserPreferences::class.java)
    }

    // Subscription converter
    @TypeConverter
    fun fromSubscription(subscription: Subscription): String {
        return gson.toJson(subscription)
    }

    @TypeConverter
    fun toSubscription(subscriptionString: String): Subscription {
        return gson.fromJson(subscriptionString, Subscription::class.java)
    }

    // DeviceProperties converter
    @TypeConverter
    fun fromDeviceProperties(properties: DeviceProperties): String {
        return gson.toJson(properties)
    }

    @TypeConverter
    fun toDeviceProperties(propertiesString: String): DeviceProperties {
        return gson.fromJson(propertiesString, DeviceProperties::class.java)
    }

    // Map<String, Any> converter
    @TypeConverter
    fun fromStringMap(map: Map<String, Any>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun toStringMap(mapString: String): Map<String, Any> {
        val type = object : TypeToken<Map<String, Any>>() {}.type
        return gson.fromJson(mapString, type)
    }

    // List<String> converter
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(listString: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(listString, type)
    }

    // List<AutomationCondition> converter
    @TypeConverter
    fun fromAutomationConditionList(list: List<AutomationCondition>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toAutomationConditionList(listString: String): List<AutomationCondition> {
        val type = object : TypeToken<List<AutomationCondition>>() {}.type
        return gson.fromJson(listString, type)
    }

    // List<AutomationAction> converter
    @TypeConverter
    fun fromAutomationActionList(list: List<AutomationAction>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toAutomationActionList(listString: String): List<AutomationAction> {
        val type = object : TypeToken<List<AutomationAction>>() {}.type
        return gson.fromJson(listString, type)
    }

    // AutomationSchedule converter
    @TypeConverter
    fun fromAutomationSchedule(schedule: AutomationSchedule?): String {
        return gson.toJson(schedule)
    }

    @TypeConverter
    fun toAutomationSchedule(scheduleString: String): AutomationSchedule? {
        return gson.fromJson(scheduleString, AutomationSchedule::class.java)
    }

    // List<TopDeviceEnergy> converter
    @TypeConverter
    fun fromTopDeviceEnergyList(list: List<TopDeviceEnergy>): String {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toTopDeviceEnergyList(listString: String): List<TopDeviceEnergy> {
        val type = object : TypeToken<List<TopDeviceEnergy>>() {}.type
        return gson.fromJson(listString, type)
    }

    // Map<String, String> converter
    @TypeConverter
    fun fromStringStringMap(map: Map<String, String>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun toStringStringMap(mapString: String): Map<String, String> {
        val type = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(mapString, type)
    }

    // Map<String, Int> converter
    @TypeConverter
    fun fromStringIntMap(map: Map<String, Int>): String {
        return gson.toJson(map)
    }

    @TypeConverter
    fun toStringIntMap(mapString: String): Map<String, Int> {
        val type = object : TypeToken<Map<String, Int>>() {}.type
        return gson.fromJson(mapString, type)
    }
}
