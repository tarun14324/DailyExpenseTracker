package com.example.dailyexpensetracker.domain.repository

import com.example.dailyexpensetracker.domain.model.User

interface UserRepository {
    suspend fun login(username: String, password: String): Boolean
    suspend fun signup(username: String, password: String): Boolean
    suspend fun isLoggedIn(): Boolean
    suspend fun setUserProfile(user: User)
    suspend fun clearAllUserData()
}