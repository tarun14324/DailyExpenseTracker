package com.example.dailyexpensetracker.data.local


data class ExpenseSummary(
    val type: String="",
    val date: String,
    val totalAmount: Double =0.0
)