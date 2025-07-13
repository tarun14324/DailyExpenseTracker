package com.example.dailyexpensetracker.core.states

sealed class HomeUiEvent{
    data object OnAddExpenseClicked : HomeUiEvent()
    data object OnAddIncomeClicked : HomeUiEvent()
    data object OnSeeAllClicked : HomeUiEvent()
}