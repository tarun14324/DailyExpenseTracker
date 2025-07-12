package com.example.dailyexpensetracker.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.dailyexpensetracker.domain.model.User
import com.example.dailyexpensetracker.domain.repository.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject


class UserPreferencesImpl @Inject constructor(
    @ApplicationContext context: Context
): UserPreferences {
    private val Context.dataStore by preferencesDataStore("user_prefs")
    private val dataStore = context.dataStore

    companion object {
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val PROFILE = stringPreferencesKey("profile")    }

    override suspend fun isLoggedIn(): Boolean {
        return dataStore.data
            .map { preferences -> preferences[IS_LOGGED_IN] ?: false }
            .first()
    }

    override suspend fun setProfile(user: User) {
        val json = Json.encodeToString(User.serializer(), user)
        dataStore.edit { preferences ->
            preferences[PROFILE] = json
            preferences[IS_LOGGED_IN] = true
        }
    }

    override suspend fun getProfile(): User {
        val json = dataStore.data.map { it[PROFILE] }.first()
        return if (json != null) {
            Json.decodeFromString(User.serializer(), json)
        } else {
            throw IllegalStateException("Profile not found")
        }
    }
}