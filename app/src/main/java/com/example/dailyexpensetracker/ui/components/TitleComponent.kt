package com.example.dailyexpensetracker.ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.example.dailyexpensetracker.core.extensions.ExpenseText
import com.example.dailyexpensetracker.ui.theme.LightGrey
import com.example.dailyexpensetracker.ui.theme.size10
import com.example.dailyexpensetracker.ui.theme.textSize12

@Composable
fun TitleComponent(title: String) {
    title.uppercase().ExpenseText(
        fontSize = textSize12,
        fontWeight = FontWeight.Medium,
        color = LightGrey
    )
    Spacer(modifier = Modifier.size(size10))
}