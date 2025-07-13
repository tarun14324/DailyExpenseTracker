package com.example.dailyexpensetracker.core.states

data class TransactionFilterState(
    val selectedDate: Long? = null,
    val groupType: GroupType = GroupType.DATE
)