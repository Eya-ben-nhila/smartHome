package com.smarthome.data.repository

import com.smarthome.data.model.LoginRequest
import com.smarthome.data.model.LoginResponse
import com.smarthome.data.model.User
import com.smarthome.data.database.entities.UserEntity
import retrofit2.Response

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<LoginResponse>
    suspend fun logout(): Result<Unit>
    suspend fun getCurrentUser(): Result<User>
    suspend fun refreshToken(): Result<LoginResponse>
    suspend fun isUserLoggedIn(): Boolean
    suspend fun saveUserToCache(user: User)
    suspend fun getCachedUser(): User?
}

class AuthRepositoryImpl(
    private val apiService: ApiService,
    private val userDao: UserDao,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(email, password))
            
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                
                // Save tokens securely
                tokenManager.saveTokens(
                    accessToken = loginResponse.token,
                    refreshToken = null, // Backend doesn't provide refresh token yet
                    expiresAt = loginResponse.expiresAt
                )
                
                // Save user info
                tokenManager.saveUserInfo(loginResponse.user.toString())
                
                // Cache user in database
                saveUserToCache(loginResponse.user)
                
                Result.success(loginResponse)
            } else {
                Result.failure(Exception("Login failed: ${response.message()}"))
            }
        } catch (e: Exception) {
            // Fallback to offline mode with cached credentials
            val cachedUser = getCachedUser()
            if (cachedUser != null && email == cachedUser.email) {
                // Create mock response for offline mode
                val offlineResponse = LoginResponse(
                    success = true,
                    token = "offline_token_${System.currentTimeMillis()}",
                    user = cachedUser,
                    message = "Offline mode - using cached credentials",
                    expiresAt = (System.currentTimeMillis() + 86400000).toString() // 24 hours
                )
                Result.success(offlineResponse)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            // Clear tokens
            tokenManager.clearTokens()
            
            // Clear cached user data if needed
            // userDao.deleteAllUsers()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCurrentUser(): Result<User> {
        return try {
            val response = apiService.getProfile()
            
            if (response.isSuccessful && response.body() != null) {
                val profileResponse = response.body()!!
                Result.success(profileResponse.user)
            } else {
                // Fallback to cached user
                val cachedUser = getCachedUser()
                if (cachedUser != null) {
                    Result.success(cachedUser)
                } else {
                    Result.failure(Exception("No user data available"))
                }
            }
        } catch (e: Exception) {
            // Fallback to cached user
            val cachedUser = getCachedUser()
            if (cachedUser != null) {
                Result.success(cachedUser)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun refreshToken(): Result<LoginResponse> {
        return try {
            // For now, we don't have refresh token support in backend
            // This would be implemented when backend supports token refresh
            Result.failure(Exception("Token refresh not implemented"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isUserLoggedIn(): Boolean {
        return try {
            val token = tokenManager.getToken()
            val isExpired = tokenManager.isTokenExpired()
            token != null && !isExpired
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun saveUserToCache(user: User) {
        try {
            val userEntity = UserEntity(
                id = user.id,
                name = user.name,
                email = user.email,
                phone = user.phone,
                avatar = user.avatar,
                createdAt = user.createdAt,
                lastLogin = user.lastLogin,
                preferences = user.preferences,
                subscription = user.subscription,
                isCached = true,
                cacheTimestamp = System.currentTimeMillis()
            )
            userDao.insertUser(userEntity)
        } catch (e: Exception) {
            // Handle cache save error
        }
    }

    override suspend fun getCachedUser(): User? {
        return try {
            val userEntity = userDao.getUserByEmail("") // Get first cached user
            userEntity?.let { entity ->
                User(
                    id = entity.id,
                    name = entity.name,
                    email = entity.email,
                    phone = entity.phone,
                    avatar = entity.avatar,
                    createdAt = entity.createdAt,
                    lastLogin = entity.lastLogin,
                    preferences = entity.preferences,
                    subscription = entity.subscription
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}
