package com.example.dailyexpensetracker.domain.usecase

import com.example.dailyexpensetracker.domain.repository.DataBaseRepository
import javax.inject.Inject

class GetDbExpenseUseCase @Inject constructor(
    private val repository: DataBaseRepository
)