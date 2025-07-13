package com.example.dailyexpensetracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dailyexpensetracker.R
import com.example.dailyexpensetracker.core.extensions.ExpenseText
import com.example.dailyexpensetracker.data.local.ExpenseEntity
import com.example.dailyexpensetracker.ui.components.NoDataView
import com.example.dailyexpensetracker.ui.theme.size12
import com.example.dailyexpensetracker.ui.theme.size16
import com.example.dailyexpensetracker.ui.theme.size30
import com.example.dailyexpensetracker.ui.theme.size4
import com.example.dailyexpensetracker.ui.theme.size6
import com.example.dailyexpensetracker.ui.viewmodel.TransactionViewModel

@Composable
fun AllTransactionsScreen(navController: NavController) {
    // Get ViewModel instance via Hilt DI
    val viewModel: TransactionViewModel = hiltViewModel()

    // Collect the list of transactions
    val transactions = viewModel.expenses.collectAsState(emptyList())

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Spacer(Modifier.height(size30))

        // Top row with back icon and screen title
        Row(modifier = Modifier.padding(horizontal = size16)) {
            // Back button to navigate back in the stack
            Icon(
                painter = painterResource(R.drawable.ic_back),
                contentDescription = stringResource(R.string.empty),
                modifier = Modifier.clickable { navController.popBackStack() }
            )

            // Title centered horizontally
            stringResource(R.string.all_transactions).ExpenseText(
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Show NoDataView if the transaction list is empty
        if (transactions.value.isEmpty()) {
            NoDataView()
        } else {
            // LazyColumn to display the list of transactions efficiently
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(size16),
                verticalArrangement = Arrangement.spacedBy(size12) // Space between items
            ) {
                items(transactions.value) { transaction ->
                    // Composable for each individual transaction item with delete functionality
                    TransactionItem(
                        transaction = transaction,
                        onDelete = { viewModel.deleteTransaction(transaction) } // Delete handler
                    )
                }
            }
        }
    }
}

@Composable
fun TransactionItem(
    transaction: ExpenseEntity,
    onDelete: () -> Unit   // Callback for delete button pressed
) {
    Card(
        shape = RoundedCornerShape(size12),                // Rounded corners for the card
        elevation = CardDefaults.cardElevation(defaultElevation = size4), // Elevation for shadow
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,  // Background color from theme
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer   // Text/icon color from theme
        ),
        modifier = Modifier.fillMaxWidth()  // Take full width available
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(size16),        // Padding inside card content
            verticalAlignment = Alignment.CenterVertically,  // Center vertically
            horizontalArrangement = Arrangement.SpaceBetween  // Space between content and delete icon
        ) {
            // Column for transaction details (title and amount)
            Column(
                verticalArrangement = Arrangement.spacedBy(size6) // Space between text rows
            ) {
                // Title row: label + transaction title
                Row {
                    "Title: ".ExpenseText(
                        style = MaterialTheme.typography.bodyMedium
                    )
                    transaction.title.ExpenseText(
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                // Amount row: label + transaction amount with conditional color and sign
                Row {
                    "Amount: ".ExpenseText(
                        style = MaterialTheme.typography.bodyMedium
                    )
                    "${if (transaction.category =="Income") "+" else "-"}${kotlin.math.abs(transaction.amount)}".ExpenseText(
                        color = if (transaction.category =="Income") Color.Green else Color.Red,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }

            // Delete button as an IconButton with a trash icon
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}




