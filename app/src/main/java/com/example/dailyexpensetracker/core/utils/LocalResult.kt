package com.example.dailyexpensetracker.core.utils

sealed class LocalResult<out T> {
    data class Success<out T>(val data: T) : LocalResult<T>()
    data class Error(val message: String?) : LocalResult<Nothing>()
    object Loading : LocalResult<Nothing>()
}