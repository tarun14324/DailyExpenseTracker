package com.example.dailyexpensetracker.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.dailyexpensetracker.R
import com.example.dailyexpensetracker.core.extensions.ExpenseText
import com.example.dailyexpensetracker.ui.theme.size24

// place holder screen for empty data
@Composable
fun NoDataView(
    modifier:Modifier = Modifier.fillMaxSize(),
    title: String = stringResource(R.string.no_data)
) {
    Box(
        modifier = Modifier
            .padding(size24),
        contentAlignment = Alignment.Center
    ) {
        title.ExpenseText(
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 4,
            modifier = Modifier.padding(horizontal = size24)
        )
    }
}