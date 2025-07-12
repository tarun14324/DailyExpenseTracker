package com.example.dailyexpensetracker.domain.repository

import com.example.dailyexpensetracker.domain.model.User

interface UserPreferences {
    suspend fun isLoggedIn():Boolean
    suspend fun setProfile(user:User)
    suspend fun getProfile():User
}