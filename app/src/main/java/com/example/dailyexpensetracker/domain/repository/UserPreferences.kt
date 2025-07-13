package com.example.dailyexpensetracker.domain.repository

import com.example.dailyexpensetracker.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserPreferences {
    suspend fun isLoggedIn():Boolean
    suspend fun setProfile(user:User)
    suspend fun getProfile():User
    suspend fun clearProfile()
    suspend fun setDarkTheme(isDark: Boolean)
    suspend fun isDarkThemeFlow(): Flow<Boolean>
}