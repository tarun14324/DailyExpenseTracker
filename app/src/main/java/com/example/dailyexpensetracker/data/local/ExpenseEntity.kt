package com.example.dailyexpensetracker.data.local

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Int?,
    val title: String,
    val amount: Double,
    val date: String,
    val category: String,
    val note: String,
    val imageUrl:Uri?,
    val timestamp: Long = System.currentTimeMillis()
)