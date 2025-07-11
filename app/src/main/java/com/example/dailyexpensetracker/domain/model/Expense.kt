package com.example.dailyexpensetracker.domain.model

data class Expense(
    val id: Int = 0,
    val title: String,
    val amount: Double,
    val category: String,
    val date: String,
)