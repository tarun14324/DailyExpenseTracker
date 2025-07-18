package com.example.dailyexpensetracker.di

import android.content.Context
import com.example.dailyexpensetracker.data.local.ExpenseDao
import com.example.dailyexpensetracker.data.local.ExpenseDatabase
import com.example.dailyexpensetracker.data.local.UserDao
import com.example.dailyexpensetracker.data.repository.DataBaseRepositoryImpl
import com.example.dailyexpensetracker.data.repository.UserPreferencesImpl
import com.example.dailyexpensetracker.data.repository.UserRepositoryImpl
import com.example.dailyexpensetracker.domain.repository.DataBaseRepository
import com.example.dailyexpensetracker.domain.repository.UserPreferences
import com.example.dailyexpensetracker.domain.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provides a singleton instance of the Room database
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ExpenseDatabase {
        return ExpenseDatabase.getInstance(context)
    }

    // Provides ExpenseDao from the database instance
    @Provides
    fun provideExpenseDao(database: ExpenseDatabase): ExpenseDao {
        return database.expenseDao()
    }

    // Provides UserDao from the database instance
    @Provides
    fun provideUserDao(database: ExpenseDatabase): UserDao = database.userDao()

    // Provides UserPreferences implementation using application context
    @Provides
    fun provideUserUserPreferences(@ApplicationContext context: Context): UserPreferences = UserPreferencesImpl(context)

    // Provides UserRepository by injecting UserDao and UserPreferences
    @Provides
    fun provideUserRepository(userDao: UserDao, userPreferences: UserPreferences): UserRepository {
        return UserRepositoryImpl(userDao, userPreferences)
    }

    // Provides DataBaseRepository by injecting ExpenseDao
    @Provides
    fun provideDataBaseRepository(expenseDao: ExpenseDao): DataBaseRepository {
        return DataBaseRepositoryImpl(expenseDao)
    }
}
