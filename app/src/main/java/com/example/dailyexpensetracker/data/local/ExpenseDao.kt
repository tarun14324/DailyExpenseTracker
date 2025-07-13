package com.example.dailyexpensetracker.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expenses")
    fun getAllExpense(): Flow<List<ExpenseEntity>>

    @Query("SELECT category AS type, date, SUM(amount) AS totalAmount FROM expenses GROUP BY category, date")
    fun getAllExpenseByDate(): Flow<List<ExpenseSummary>>

    @Query("SELECT COUNT(*) > 0 FROM expenses WHERE title = :name AND amount = :amount AND category = :category")
    suspend fun isExpenseExist(name: String, amount: Double, category: String): Boolean

    @Query("DELETE FROM expenses")
    suspend fun clearAllUserData()

    @Insert
    suspend fun insertExpense(expenseEntity: ExpenseEntity):Long

    @Delete
    suspend fun deleteExpense(expenseEntity: ExpenseEntity)
}