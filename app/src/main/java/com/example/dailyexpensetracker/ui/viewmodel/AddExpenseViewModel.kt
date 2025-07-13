package com.example.dailyexpensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyexpensetracker.data.local.ExpenseEntity
import com.example.dailyexpensetracker.core.states.InsertExpenseResult
import com.example.dailyexpensetracker.domain.repository.DataBaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val repository: DataBaseRepository,
) : ViewModel() {

    // Holds the result of the insert operation (success or failure)
    private val _insertResult = MutableStateFlow<InsertExpenseResult?>(null)
    val insertResult: StateFlow<InsertExpenseResult?> = _insertResult.asStateFlow()

    // Called when user confirms adding an expense
    fun onAddExpenseClicked(expenseEntity: ExpenseEntity) {
        viewModelScope.launch {
            // Insert expense into database and update result state
            val success = repository.insertExpense(expenseEntity)
            _insertResult.value = success
        }
    }

    // Clears the insert result state after it's been handled
    fun resetInsertResult() {
        _insertResult.value = null
    }
}
