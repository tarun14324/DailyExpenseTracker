package com.example.dailyexpensetracker.ui.navigation

object NavRouts {
    const val HOME_SCREEN="homeScreen"
    const val ADD_EXPENSE_SCREEN="addExpenseScreen"
    const val ARG_IS_INCOME = "isIncome"
    const val ADD_EXPENSE_SCREEN_ROUTE = "$ADD_EXPENSE_SCREEN/{$ARG_IS_INCOME}"
    const val LOGIN_SCREEN="loginScreen"
    const val SIGNUP_SCREEN="signupScreen"
    const val REPORT_SCREEN="reportScreen"
    const val PROFILE_SCREEN="profileScreen"
    const val ALL_TRANSACTIONS="allTransactions"
}