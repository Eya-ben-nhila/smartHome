package com.smarthome.data.api.model

data class BackendAuthResponse(
    val message: String,
    val token: String?,
    val userId: String?,
    val user: BackendUser?
)

data class BackendUser(
    val id: String,
    val email: String,
    val fullName: String,
    val role: String
)

data class BackendLoginRequest(
    val email: String,
    val password: String
)

data class BackendRegisterRequest(
    val fullName: String,
    val email: String,
    val password: String
)
