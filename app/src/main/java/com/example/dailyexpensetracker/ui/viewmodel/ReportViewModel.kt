package com.example.dailyexpensetracker.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dailyexpensetracker.data.local.ExpenseSummary
import com.example.dailyexpensetracker.domain.repository.DataBaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val dataBaseRepository: DataBaseRepository
) : ViewModel() {

    // Holds a list of summarized expenses for reporting
    private val _reportsList = MutableStateFlow<List<ExpenseSummary>>(emptyList())
    val reportsList: StateFlow<List<ExpenseSummary>> = _reportsList.asStateFlow()

    init {
        // Automatically fetch last 7 days' expense summary on ViewModel creation
        getLast7DaysExpenses()
    }

    // Collects expense summary data for the last 7 days from the repository
    private fun getLast7DaysExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            dataBaseRepository.getSevenDayaExpenses().collect { list ->
                _reportsList.value = list
            }
        }
    }
}
