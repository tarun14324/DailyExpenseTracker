package com.example.dailyexpensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyexpensetracker.data.local.ExpenseEntity
import com.example.dailyexpensetracker.domain.repository.DataBaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val dataBaseRepository: DataBaseRepository
) : ViewModel() {

    // Observes all expenses from the database
    var expenses: Flow<List<ExpenseEntity>> = dataBaseRepository.getAllExpense()

    // Deletes a specific transaction from the database
    fun deleteTransaction(item: ExpenseEntity) {
        viewModelScope.launch {
            dataBaseRepository.deleteExpense(item)
        }
    }
}
