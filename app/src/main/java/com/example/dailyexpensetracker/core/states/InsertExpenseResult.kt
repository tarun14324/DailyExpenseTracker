package com.example.dailyexpensetracker.core.states

sealed class InsertExpenseResult {
    data object Success : InsertExpenseResult()
    data object ExpenseAlreadyExists : InsertExpenseResult()
    data object Error : InsertExpenseResult()
}