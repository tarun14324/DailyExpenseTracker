package com.example.dailyexpensetracker.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.dailyexpensetracker.R
import com.example.dailyexpensetracker.core.extensions.ExpenseText
import com.example.dailyexpensetracker.ui.theme.Zinc
import com.example.dailyexpensetracker.ui.theme.size12
import com.example.dailyexpensetracker.ui.theme.size16
import com.example.dailyexpensetracker.ui.theme.size40
import com.example.dailyexpensetracker.ui.theme.size48
import com.example.dailyexpensetracker.ui.theme.size60
import com.example.dailyexpensetracker.ui.theme.size8

@Composable
fun MultiFloatingActionButton(
    modifier: Modifier,
    onAddExpenseClicked: () -> Unit,
    onAddIncomeClicked: () -> Unit
) {
    // Track whether the FAB menu is expanded or collapsed
    var expanded by remember { mutableStateOf(false) }

    // Outer box to align content at the bottom end (bottom-right)
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.spacedBy(size16) // space between buttons
        ) {
            // Secondary FABs (Add Income & Add Expense) appear only when expanded
            AnimatedVisibility(visible = expanded) {
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.padding(size16) // padding around the expanded buttons
                ) {
                    // Add Income button
                    Box(
                        modifier = Modifier
                            .height(size48)
                            .background(color = Zinc, shape = RoundedCornerShape(size12))
                            .padding(horizontal = size8)
                            .clickable {
                                onAddIncomeClicked.invoke() // trigger income click callback
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row {
                            stringResource(R.string.add_income).ExpenseText(
                                modifier = modifier.padding(end = size8)
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_income),
                                contentDescription = "Add Income",
                                tint = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(size16)) // spacing between buttons

                    // Add Expense button
                    Box(
                        modifier = Modifier
                            .height(size48)
                            .background(color = Zinc, shape = RoundedCornerShape(size12))
                            .padding(horizontal = size8)
                            .clickable {
                                onAddExpenseClicked.invoke() // trigger expense click callback
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row {
                            stringResource(R.string.add_expense).ExpenseText(
                                modifier = modifier.padding(end = size8)
                            )
                            Icon(
                                painter = painterResource(R.drawable.ic_expense),
                                contentDescription = "Add Expense",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            // Main FAB button toggles expanded state when clicked
            Box(
                modifier = Modifier
                    .padding(size8)
                    .size(size60)
                    .clip(RoundedCornerShape(size16))
                    .background(color = Zinc)
                    .clickable {
                        expanded = !expanded // toggle visibility of secondary FABs
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_addbutton),
                    contentDescription = "small floating action button",
                    modifier = Modifier.size(size40)
                )
            }
        }
    }
}
