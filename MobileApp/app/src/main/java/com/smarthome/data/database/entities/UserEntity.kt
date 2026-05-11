package com.smarthome.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    @SerializedName("id")
    val id: String,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("email")
    val email: String,
    
    @SerializedName("phone")
    val phone: String?,
    
    @SerializedName("avatar")
    val avatar: String?,
    
    @SerializedName("createdAt")
    val createdAt: String,
    
    @SerializedName("lastLogin")
    val lastLogin: String,
    
    @SerializedName("preferences")
    val preferences: UserPreferences,
    
    @SerializedName("subscription")
    val subscription: Subscription,
    
    // Local fields
    val isCached: Boolean = false,
    val cacheTimestamp: Long = System.currentTimeMillis()
)

data class UserPreferences(
    @SerializedName("theme")
    val theme: String = "light",
    
    @SerializedName("notifications")
    val notifications: Boolean = true,
    
    @SerializedName("language")
    val language: String = "en",
    
    @SerializedName("autoMode")
    val autoMode: Boolean = true
)

data class Subscription(
    @SerializedName("plan")
    val plan: String = "free",
    
    @SerializedName("status")
    val status: String = "active",
    
    @SerializedName("expiresAt")
    val expiresAt: String?,
    
    @SerializedName("features")
    val features: List<String> = emptyList()
)
