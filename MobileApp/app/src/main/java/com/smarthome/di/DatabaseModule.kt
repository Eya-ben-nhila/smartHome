package com.smarthome.di

import android.content.Context
import androidx.room.Room
import com.smarthome.data.database.SmartHomeDatabase
import com.smarthome.data.database.SmartHomeDatabase.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideSmartHomeDatabase(
        @ApplicationContext context: Context
    ): SmartHomeDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            SmartHomeDatabase::class.java,
            DATABASE_NAME
        )
        .addMigrations(MIGRATION_1_2)
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideUserDao(database: SmartHomeDatabase) = database.userDao()

    @Provides
    fun provideDeviceDao(database: SmartHomeDatabase) = database.deviceDao()

    @Provides
    fun provideEnergyDao(database: SmartHomeDatabase) = database.energyDao()

    @Provides
    fun provideSecurityDao(database: SmartHomeDatabase) = database.securityDao()

    @Provides
    fun provideAutomationDao(database: SmartHomeDatabase) = database.automationDao()
}
