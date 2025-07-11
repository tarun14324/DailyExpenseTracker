package com.example.dailyexpensetracker.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val title: String,
    val amount: Double,
    val date: String,
    val category: String
)