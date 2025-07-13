package com.example.dailyexpensetracker.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import com.example.dailyexpensetracker.data.local.ExpenseSummary
import com.example.dailyexpensetracker.ui.theme.Zinc
import kotlin.math.abs

@Composable
fun LineChartWithPoints(
    data: Map<String, List<ExpenseSummary>>,
    modifier: Modifier = Modifier
) {
    // Calculate total expense per date/category
    val totals = data.mapValues { it.value.sumOf { e -> e.totalAmount } }
    val maxTotal = totals.maxOfOrNull { it.value } ?: 1.0
    val minTotal = 0.0

    // Prepare points as list of index to total amount pairs
    val points = totals.entries.mapIndexed { index, entry -> index to entry.value }
    val labels = totals.keys.toList()

    val spacing = 60f       // Left spacing for y-axis labels
    val pointRadius = 16f   // Radius for data points

    // Paint for drawing text on Canvas
    val textPaint = remember {
        android.graphics.Paint().apply {
            color = android.graphics.Color.LTGRAY
            textSize = 30f
            textAlign = android.graphics.Paint.Align.CENTER
        }
    }

    // Track currently selected point index for tooltip display
    var selectedPointIndex by remember { mutableStateOf<Int?>(0) }
    val animationProgress = remember { Animatable(0f) }

    // Animate chart drawing on screen entry
    LaunchedEffect(points) {
        animationProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
        )
    }

    Box(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                // Detect tap to select closest point and show tooltip
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        val tappedX = offset.x
                        val closestIndex = points.indices.minByOrNull { i ->
                            val xGap = (size.width - spacing) / (points.size - 1).coerceAtLeast(1)
                            val x = spacing + i * xGap
                            abs(tappedX - x)
                        }
                        selectedPointIndex = closestIndex
                    }
                }
        ) {
            val graphWidth = size.width - spacing
            val graphHeight = size.height - spacing

            // Draw horizontal grid lines and y-axis labels
            val stepY = graphHeight / 4
            val stepValue = (maxTotal - minTotal) / 4
            for (i in 0..4) {
                val y = i * stepY
                drawLine(
                    color = Color.LightGray,
                    start = Offset(spacing, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
                drawContext.canvas.nativeCanvas.drawText(
                    "₹${"%.0f".format(maxTotal - (i * stepValue))}",
                    0f,
                    y + 10f,
                    textPaint
                )
            }

            // Draw x-axis line
            drawLine(
                color = Color.Gray,
                start = Offset(spacing, graphHeight),
                end = Offset(size.width, graphHeight),
                strokeWidth = 2f
            )

            // Calculate x-axis gap between points
            val xGap = graphWidth / (points.size - 1).coerceAtLeast(1)

            // Calculate position of points scaled to maxTotal and animation progress
            val graphPoints = points.map { (index, value) ->
                val x = spacing + index * xGap
                val y = graphHeight * (1 - (value / maxTotal)).toFloat() * animationProgress.value
                Offset(x, y)
            }

            // Draw connecting line between points
            for (i in 0 until graphPoints.size - 1) {
                drawLine(
                    color = Zinc,
                    start = graphPoints[i],
                    end = graphPoints[i + 1],
                    strokeWidth = 4f
                )
            }

            // Draw circles at each data point (outer dark gray circle)
            graphPoints.forEach { point ->
                drawCircle(
                    color = Color.DarkGray,
                    radius = pointRadius + 2f,
                    center = point
                )
            }

            // Draw x-axis labels (first 5 chars)
            labels.forEachIndexed { index, label ->
                val x = spacing + index * xGap
                drawContext.canvas.nativeCanvas.drawText(
                    label.take(5),
                    x,
                    graphHeight + 40f,
                    textPaint
                )
            }

            // Draw tooltip for selected point showing exact value
            selectedPointIndex?.let { index ->
                if (index in graphPoints.indices) {
                    val point = graphPoints[index]
                    val value = points[index].second
                    val tooltipWidth = 120f
                    val tooltipHeight = 50f

                    drawRoundRect(
                        color = Color.DarkGray,
                        topLeft = Offset(point.x - tooltipWidth / 2, point.y - 80),
                        size = Size(tooltipWidth, tooltipHeight),
                        cornerRadius = CornerRadius(8f, 8f)
                    )
                    drawContext.canvas.nativeCanvas.drawText(
                        "₹${"%.2f".format(value)}",
                        point.x,
                        point.y - 50,
                        textPaint
                    )
                }
            }
        }
    }
}
