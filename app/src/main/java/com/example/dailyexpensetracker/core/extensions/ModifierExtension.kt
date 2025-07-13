package com.example.dailyexpensetracker.core.extensions

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.verticalGradientBackground(
    colors: List<Color> = listOf(
        Color(0xFF429690),
        Color(0xFF2A7C76)
    )
): Modifier = this.then(
    Modifier.drawBehind {
        val brush = Brush.verticalGradient(
            colors = colors,
            startY = 0f,
            endY = size.height
        )
        drawRect(brush = brush)
    }
)