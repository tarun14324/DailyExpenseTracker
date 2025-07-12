package com.example.dailyexpensetracker.core.utils

sealed class LocalResult<out T> {
    data class Success<out T>(val data: T) : LocalResult<T>()
    data class Error(val message: String?) : LocalResult<Nothing>()
    object Loading : LocalResult<Nothing>()
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}