package com.smarthome.di

import com.smarthome.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: ApiService,
        userDao: com.smarthome.data.database.UserDao,
        tokenManager: TokenManager
    ): AuthRepository {
        return AuthRepositoryImpl(
            apiService = apiService,
            userDao = userDao,
            tokenManager = tokenManager
        )
    }

    @Provides
    @Singleton
    fun provideDeviceRepository(
        apiService: ApiService,
        deviceDao: com.smarthome.data.database.DeviceDao
    ): DeviceRepository {
        return DeviceRepositoryImpl(
            apiService = apiService,
            deviceDao = deviceDao
        )
    }

    @Provides
    @Singleton
    fun provideEnergyRepository(
        apiService: ApiService,
        energyDao: com.smarthome.data.database.EnergyDao
    ): EnergyRepository {
        return EnergyRepositoryImpl(
            apiService = apiService,
            energyDao = energyDao
        )
    }

    @Provides
    @Singleton
    fun provideSecurityRepository(
        apiService: ApiService,
        securityDao: com.smarthome.data.database.SecurityDao
    ): SecurityRepository {
        return SecurityRepositoryImpl(
            apiService = apiService,
            securityDao = securityDao
        )
    }

    @Provides
    @Singleton
    fun provideAutomationRepository(
        apiService: ApiService,
        automationDao: com.smarthome.data.database.AutomationDao
    ): AutomationRepository {
        return AutomationRepositoryImpl(
            apiService = apiService,
            automationDao = automationDao
        )
    }

    @Provides
    @Singleton
    fun provideProfileRepository(
        apiService: ApiService,
        userDao: com.smarthome.data.database.UserDao
    ): ProfileRepository {
        return ProfileRepositoryImpl(
            apiService = apiService,
            userDao = userDao
        )
    }

    @Provides
    @Singleton
    fun provideAlertRepository(
        apiService: ApiService
    ): AlertRepository {
        return AlertRepositoryImpl(apiService)
    }
}
