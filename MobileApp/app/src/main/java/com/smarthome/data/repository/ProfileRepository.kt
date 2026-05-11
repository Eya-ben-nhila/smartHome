package com.smarthome.data.repository

import com.smarthome.data.model.*
import retrofit2.Response

interface ProfileRepository {
    suspend fun getProfile(): Result<User>
    suspend fun updateProfile(user: User): Result<User>
    suspend fun updatePreferences(preferences: UserPreferences): Result<UserPreferences>
    suspend fun getCachedProfile(): Result<User?>
    suspend fun cacheProfile(user: User)
}

class ProfileRepositoryImpl(
    private val apiService: ApiService,
    private val userDao: com.smarthome.data.database.UserDao
) : ProfileRepository {

    override suspend fun getProfile(): Result<User> {
        return try {
            val response = apiService.getProfile()
            
            if (response.isSuccessful && response.body() != null) {
                val profileResponse = response.body()!!
                
                // Cache user profile locally
                cacheProfile(profileResponse.user)
                
                Result.success(profileResponse.user)
            } else {
                // Fallback to cached profile
                val cachedProfile = getCachedProfile()
                if (cachedProfile.isSuccess && cachedProfile.getOrNull() != null) {
                    Result.success(cachedProfile.getOrNull()!!)
                } else {
                    Result.failure(Exception("No profile data available"))
                }
            }
        } catch (e: Exception) {
            // Network error, fallback to cached profile
            val cachedProfile = getCachedProfile()
            if (cachedProfile.isSuccess && cachedProfile.getOrNull() != null) {
                Result.success(cachedProfile.getOrNull()!!)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun updateProfile(user: User): Result<User> {
        return try {
            // For now, we'll just update the cache
            // In a real implementation, you'd call the API to update the profile
            cacheProfile(user)
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePreferences(preferences: UserPreferences): Result<UserPreferences> {
        return try {
            // Get current user
            val currentUser = getProfile().getOrNull() ?: return Result.failure(Exception("No user found"))
            
            // Update preferences
            val updatedUser = currentUser.copy(preferences = preferences)
            
            // Save updated user
            cacheProfile(updatedUser)
            
            Result.success(preferences)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getCachedProfile(): Result<User?> {
        return try {
            val cachedUser = userDao.getUserByEmail("") // Get first cached user
            val user = cachedUser?.let { entity ->
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
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun cacheProfile(user: User) {
        try {
            val userEntity = com.smarthome.data.database.entities.UserEntity(
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
            // Handle cache error silently
        }
    }
}
