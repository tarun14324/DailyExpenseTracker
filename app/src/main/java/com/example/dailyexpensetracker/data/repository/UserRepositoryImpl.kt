package com.example.dailyexpensetracker.data.repository

import com.example.dailyexpensetracker.data.local.UserDao
import com.example.dailyexpensetracker.data.local.UserEntity
import com.example.dailyexpensetracker.domain.model.User
import com.example.dailyexpensetracker.domain.repository.UserPreferences
import com.example.dailyexpensetracker.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userDao: UserDao,
    private val userPreferences: UserPreferences
) : UserRepository {

    // Attempts to login by checking if a user exists with the given username and password
    override suspend fun login(username: String, password: String): Boolean {
        val user = userDao.getUser(username, password)
        return user != null  // Returns true if user found, false otherwise
    }

    // Signs up a new user if the username does not already exist
    override suspend fun signup(username: String, password: String): Boolean {
        val existingUser = userDao.isUserExist(username)
        return if (existingUser == null) {
            userDao.insertUser(UserEntity(username, password))  // Inserts new user
            true
        } else {
            false  // User already exists, signup fails
        }
    }

    // Returns login status by querying UserPreferences
    override suspend fun isLoggedIn(): Boolean = userPreferences.isLoggedIn()

    // Saves the current user profile to UserPreferences
    override suspend fun setUserProfile(user: User) {
        userPreferences.setProfile(user)
    }

    // Clears all user-related data from the database
    override suspend fun clearAllUserData() {
        userDao.clearAllUserData()
    }
}
