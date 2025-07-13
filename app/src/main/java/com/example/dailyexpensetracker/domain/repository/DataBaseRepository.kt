package com.example.dailyexpensetracker.domain.repository

import com.example.dailyexpensetracker.data.local.ExpenseEntity
import com.example.dailyexpensetracker.data.local.ExpenseSummary
import com.example.dailyexpensetracker.core.states.InsertExpenseResult
import kotlinx.coroutines.flow.Flow

interface DataBaseRepository {
    suspend fun insertExpense(expenseEntity: ExpenseEntity): InsertExpenseResult
    suspend fun deleteExpense(expenseEntity: ExpenseEntity)
    suspend fun clearAllUserData()
    fun getAllExpense(): Flow<List<ExpenseEntity>>
    suspend fun getSevenDayaExpenses():  Flow<List<ExpenseSummary>>
}