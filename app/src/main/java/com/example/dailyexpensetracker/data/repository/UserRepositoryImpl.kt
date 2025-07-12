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

    override suspend fun login(username: String, password: String): Boolean {
        val user = userDao.getUser(username, password)
        return user != null
    }

    override suspend fun signup(username: String, password: String): Boolean {
        val existingUser = userDao.isUserExist(username)
        return if (existingUser == null) {
            userDao.insertUser(UserEntity(username, password))
            true
        } else {
            false
        }
    }

    override suspend fun isLoggedIn(): Boolean = userPreferences.isLoggedIn()
    override suspend fun setUserProfile(user: User) {
        userPreferences.setProfile(user)
    }
}