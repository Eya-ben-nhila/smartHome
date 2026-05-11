package com.smarthome.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    
    @SerializedName("password")
    val password: String
)

data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("token")
    val token: String,
    
    @SerializedName("user")
    val user: User,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("expiresAt")
    val expiresAt: String
)

data class User(
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
    val subscription: Subscription
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
