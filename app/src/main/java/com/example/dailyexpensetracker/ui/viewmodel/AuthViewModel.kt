package com.example.dailyexpensetracker.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyexpensetracker.core.utils.AuthState
import com.example.dailyexpensetracker.domain.model.User
import com.example.dailyexpensetracker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    var loginState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    var signupState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    var isLoggedIn by mutableStateOf<Boolean?>(null)
        private set

    init {
        checkLoginStatus()
    }

    private fun checkLoginStatus() {
        viewModelScope.launch {
            isLoggedIn = repository.isLoggedIn()
        }
    }

    fun login(user: User) {
        viewModelScope.launch {
            loginState = AuthState.Loading
            val result = repository.login(user.username, user.password)
            loginState = if (result) {
                repository.setUserProfile(user)
                AuthState.Success
            } else AuthState.Error("Invalid credentials")
        }
    }

    fun signup(user: User) {
        viewModelScope.launch {
            signupState = AuthState.Loading
            val result = repository.signup(user.username, user.password)
            signupState = if (result) AuthState.Success else AuthState.Error("User already exists")
        }
    }

    fun resetState() {
        loginState = AuthState.Idle
        signupState = AuthState.Idle
    }
}