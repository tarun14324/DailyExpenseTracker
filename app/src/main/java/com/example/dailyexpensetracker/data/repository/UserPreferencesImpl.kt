package com.example.dailyexpensetracker.data.repository

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.dailyexpensetracker.domain.model.User
import com.example.dailyexpensetracker.domain.repository.UserPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("user_prefs")

class UserPreferencesImpl @Inject constructor(
    @ApplicationContext context: Context
) : UserPreferences {
    // DataStore instance scoped to the application context
    private val dataStore = context.dataStore

    companion object {
        // Keys for storing data in DataStore preferences
        private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val PROFILE = stringPreferencesKey("profile")
        private val THEME_KEY = booleanPreferencesKey("theme_mode")
    }

    // Checks whether the user is logged in by reading the boolean flag from DataStore
    override suspend fun isLoggedIn(): Boolean {
        return dataStore.data
            .map { preferences -> preferences[IS_LOGGED_IN] ?: false }
            .first()  // Gets the first (current) value from the flow
    }

    // Saves the user profile as a JSON string and sets the logged-in flag to true
    override suspend fun setProfile(user: User) {
        val json = Json.encodeToString(User.serializer(), user)
        dataStore.edit { preferences ->
            preferences[PROFILE] = json        // Store user profile JSON string
            preferences[IS_LOGGED_IN] = true  // Mark user as logged in
        }
    }

    // Retrieves the user profile by decoding the JSON string from DataStore
    override suspend fun getProfile(): User {
        val json = dataStore.data.map { it[PROFILE] }.first()
        return if (json != null) {
            Json.decodeFromString(User.serializer(), json)  // Deserialize JSON to User
        } else {
            User("", "")  // Return empty User if no profile found
        }
    }

    // Clears all stored preferences, effectively logging out the user
    override suspend fun clearProfile() {
        dataStore.edit { it.clear() }
    }

    // Returns a Flow emitting the current theme preference (dark mode or not)
    override suspend fun isDarkThemeFlow(): Flow<Boolean> {
        return dataStore.data
            .map { preferences -> preferences[THEME_KEY] ?: false }
    }

    // Saves the theme preference (dark mode enabled/disabled)
    override suspend fun setDarkTheme(isDark: Boolean) {
        dataStore.edit { preferences ->
            preferences[THEME_KEY] = isDark
        }
    }
}
