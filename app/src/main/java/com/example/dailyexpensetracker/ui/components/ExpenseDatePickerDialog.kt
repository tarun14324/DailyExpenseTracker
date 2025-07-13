package com.example.dailyexpensetracker.ui.components

import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.dailyexpensetracker.R
import com.example.dailyexpensetracker.core.extensions.ExpenseText


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDatePickerDialog(
    onDateSelected: (date: Long) -> Unit, onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    val selectedDate = datePickerState.selectedDateMillis ?: 0L
    DatePickerDialog(onDismissRequest = { onDismiss() }, confirmButton = {
        TextButton(onClick = { onDateSelected(selectedDate) }) {
            stringResource(R.string.confirm).ExpenseText()
        }
    }, dismissButton = {
        TextButton(onClick = { onDateSelected(selectedDate) }) {
            stringResource(R.string.cancel).ExpenseText()
        }
    }) {
        DatePicker(state = datePickerState)
    }
}