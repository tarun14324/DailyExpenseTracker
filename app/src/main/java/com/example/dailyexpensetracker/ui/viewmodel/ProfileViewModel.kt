package com.example.dailyexpensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyexpensetracker.data.repository.UserRepositoryImpl
import com.example.dailyexpensetracker.domain.repository.DataBaseRepository
import com.example.dailyexpensetracker.domain.repository.UserPreferences
import com.example.dailyexpensetracker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userPreferences: UserPreferences,
    private val dataBaseRepository: DataBaseRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    // Holds the current username from user preferences
    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    init {
        // Load username when ViewModel is created
        getUserName()
    }

    // Retrieves the stored username from preferences
    private fun getUserName() {
        viewModelScope.launch {
            _userName.value = userPreferences.getProfile().username
        }
    }

    // Clears user session  from local storage
    fun onLogoutClicked() {
        viewModelScope.launch {
            dataBaseRepository.clearAllUserData()
            userPreferences.clearProfile()
           // userRepository.clearAllUserData()
        }
    }
}
