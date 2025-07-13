package com.example.dailyexpensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyexpensetracker.core.utils.Utils
import com.example.dailyexpensetracker.data.local.ExpenseEntity
import com.example.dailyexpensetracker.domain.repository.DataBaseRepository
import com.example.dailyexpensetracker.domain.repository.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    dataBaseRepository: DataBaseRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    // Exposes all stored expenses from the database as a Flow
    var expenses: Flow<List<ExpenseEntity>> = dataBaseRepository.getAllExpense()

    // Holds the username loaded from user preferences
    private val _userName = MutableStateFlow("")
    val userName = _userName.asStateFlow()

    // Holds the current theme state (dark/light)
    private val _isDarkTheme = MutableStateFlow<Boolean?>(null)
    val isDarkTheme = _isDarkTheme.asStateFlow()

    init {
        // Observe the saved theme preference and update local state
        viewModelScope.launch {
            userPreferences.isDarkThemeFlow().collect { savedTheme ->
                _isDarkTheme.value = savedTheme
            }
        }

        // Fetch and store the username on initialization
        getUserName()
    }

    // Saves theme change to preferences and updates state
    fun toggleTheme(isDark: Boolean) {
        _isDarkTheme.value = isDark
        viewModelScope.launch {
            userPreferences.setDarkTheme(isDark)
        }
    }

    // Loads the stored username from preferences
    private fun getUserName() {
        viewModelScope.launch {
            _userName.value = userPreferences.getProfile().username
        }
    }

    // Calculates and returns current balance from income - expenses
    fun getBalance(list: List<ExpenseEntity>): String {
        var balance = 0.0
        for (expense in list) {
            if (expense.category == "Income") {
                balance += expense.amount
            } else {
                balance -= expense.amount
            }
        }
        return Utils.formatCurrency(balance)
    }

    // Calculates total spending (excluding income entries)
    fun getTotalExpense(list: List<ExpenseEntity>): String {
        var total = 0.0
        for (expense in list) {
            if (expense.category != "Income") {
                total += expense.amount
            }
        }
        return Utils.formatCurrency(total)
    }

    // Calculates total income entries
    fun getTotalIncome(list: List<ExpenseEntity>): String {
        var totalIncome = 0.0
        for (expense in list) {
            if (expense.category == "Income") {
                totalIncome += expense.amount
            }
        }
        return Utils.formatCurrency(totalIncome)
    }
}
