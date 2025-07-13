package com.example.dailyexpensetracker.data.repository

import com.example.dailyexpensetracker.data.local.ExpenseDao
import com.example.dailyexpensetracker.data.local.ExpenseEntity
import com.example.dailyexpensetracker.data.local.ExpenseSummary
import com.example.dailyexpensetracker.core.states.InsertExpenseResult
import com.example.dailyexpensetracker.domain.repository.DataBaseRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class DataBaseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao
) : DataBaseRepository {

    // Inserts an expense after checking if it already exists based on title, amount, and category
    override suspend fun insertExpense(expenseEntity: ExpenseEntity): InsertExpenseResult {
        return try {
            // Check for existence of the same expense in the database to prevent duplicates
            val exists = expenseDao.isExpenseExist(
                expenseEntity.title,
                expenseEntity.amount,
                expenseEntity.category
            )

            if (exists) {
                // Return specific result if expense already exists
                InsertExpenseResult.ExpenseAlreadyExists
            } else {
                // Insert the expense and check if insertion was successful (rowId != -1)
                val rowId = expenseDao.insertExpense(expenseEntity)
                if (rowId != -1L) {
                    InsertExpenseResult.Success
                } else {
                    InsertExpenseResult.Error // Insertion failed for unknown reasons
                }
            }
        } catch (e: Exception) {
            // Handle any exceptions during database operation
            InsertExpenseResult.Error
        }
    }

    // Deletes a given expense entity from the database
    override suspend fun deleteExpense(expenseEntity: ExpenseEntity) {
        expenseDao.deleteExpense(expenseEntity)
    }

    // Returns a reactive stream (Flow) of all expenses to observe changes in real-time
    override fun getAllExpense(): Flow<List<ExpenseEntity>> {
        return expenseDao.getAllExpense()
    }

    // Clears all expense data for the user from the database
    override suspend fun clearAllUserData() {
        expenseDao.clearAllUserData()
    }

    // Retrieves a Flow of expense summaries for the last 7 days grouped by date or other criteria
    override suspend fun getSevenDayaExpenses(): Flow<List<ExpenseSummary>> {
        return expenseDao.getAllExpenseByDate()
    }
}
