package com.example.dailyexpensetracker.core.extensions

import com.example.dailyexpensetracker.data.local.ExpenseEntity
import com.example.dailyexpensetracker.domain.model.Expense

fun ExpenseEntity.toDomain(): Expense {
    return Expense(
        id = this.id ?: 0,
        title = this.title,
        amount = this.amount,
        category = this.category,
        date = this.date
    )
}

fun Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = this.id,
        title = this.title,
        amount = this.amount,
        category = this.category,
        date = this.date
    )
}