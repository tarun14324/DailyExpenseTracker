package com.example.dailyexpensetracker.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyexpensetracker.core.states.AuthState
import com.example.dailyexpensetracker.core.states.HomeUiEvent
import com.example.dailyexpensetracker.domain.model.User
import com.example.dailyexpensetracker.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    // Tracks the current login process state
    var loginState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    // Tracks the current signup process state
    var signupState by mutableStateOf<AuthState>(AuthState.Idle)
        private set

    // Holds login status (null = checking, true/false = known)
    var isLoggedIn by mutableStateOf<Boolean?>(null)
        private set

    // Emits navigation events to trigger actions from other composables
    private val _navigationEvent = MutableSharedFlow<HomeUiEvent>(replay = 0, extraBufferCapacity = 1)
    val navigationEvent: SharedFlow<HomeUiEvent> = _navigationEvent.asSharedFlow()

    init {
        // Check login status on ViewModel creation
        checkLoginStatus()
    }

    // Queries repository to determine if user is already logged in
    private fun checkLoginStatus() {
        viewModelScope.launch {
            isLoggedIn = repository.isLoggedIn()
        }
    }

    // Performs login logic, updates state based on result
    fun login(user: User) {
        viewModelScope.launch {
            loginState = AuthState.Loading
            val result = repository.login(user.username, user.password)
            loginState = if (result) {
                repository.setUserProfile(user) // Save user session/profile
                AuthState.Success
            } else {
                AuthState.Error("Invalid credentials")
            }
        }
    }

    // Performs signup logic, updates state based on result
    fun signup(user: User) {
        viewModelScope.launch {
            signupState = AuthState.Loading
            val result = repository.signup(user.username, user.password)
            signupState = if (result) {
                AuthState.Success
            } else {
                AuthState.Error("User already exists")
            }
        }
    }

    // Resets both login and signup states to idle
    fun resetState() {
        loginState = AuthState.Idle
        signupState = AuthState.Idle
    }

    // Triggers a UI event (e.g., navigate to another screen)
    fun sendEvent(event: HomeUiEvent){
        _navigationEvent.tryEmit(event)
    }
}
