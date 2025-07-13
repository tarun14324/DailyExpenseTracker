package com.example.dailyexpensetracker.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.dailyexpensetracker.R
import com.example.dailyexpensetracker.core.extensions.ExpenseText
import com.example.dailyexpensetracker.core.extensions.generatePdfReport
import com.example.dailyexpensetracker.core.extensions.saveFileToDownloads
import com.example.dailyexpensetracker.core.extensions.sharePdf
import com.example.dailyexpensetracker.data.local.ExpenseSummary
import com.example.dailyexpensetracker.ui.components.LineChartWithPoints
import com.example.dailyexpensetracker.ui.navigation.NavRouts.ADD_EXPENSE_SCREEN
import com.example.dailyexpensetracker.ui.theme.Zinc
import com.example.dailyexpensetracker.ui.theme.size10
import com.example.dailyexpensetracker.ui.theme.size12
import com.example.dailyexpensetracker.ui.theme.size16
import com.example.dailyexpensetracker.ui.theme.size2
import com.example.dailyexpensetracker.ui.theme.size24
import com.example.dailyexpensetracker.ui.theme.size30
import com.example.dailyexpensetracker.ui.theme.size4
import com.example.dailyexpensetracker.ui.theme.size40
import com.example.dailyexpensetracker.ui.theme.size400
import com.example.dailyexpensetracker.ui.theme.size8
import com.example.dailyexpensetracker.ui.viewmodel.ReportViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(navController: NavController) {
    val viewModel: ReportViewModel = hiltViewModel()
    // Collect report data as State
    val reportData = viewModel.reportsList.collectAsState()
    val context = LocalContext.current

    // Group reports by date and by category/type for display and chart
    val groupedByDate = reportData.value.groupBy { it.date }
    val groupedByCategory = reportData.value.groupBy { it.type }

    Scaffold(
        topBar = {
            TopAppBar(
                // Top bar with title and action icons
                title = { Text(stringResource(R.string.seven_days_reports)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    // Show share and download icons only if data exists
                    if (groupedByDate.isNotEmpty()) {
                        // Generate PDF report from current data
                        val pdfFile = context.generatePdfReport(reportData.value)

                        // Share icon triggers sharing of the generated PDF
                        Icon(
                            Icons.Default.Share,
                            contentDescription = null,
                            Modifier
                                .padding(end = size16)
                                .size(size24)
                                .clickable {
                                    context.sharePdf(pdfFile)
                                })

                        // Download icon triggers saving PDF to Downloads folder
                        Icon(
                            Icons.Default.Download,
                            contentDescription = null,
                            Modifier
                                .padding(end = size16)
                                .size(size24)
                                .clickable {
                                    context.saveFileToDownloads(
                                        pdfFile,
                                        "last_7_days_expense_report.pdf"
                                    )
                                })
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(size16)
                .verticalScroll(rememberScrollState())
        ) {
            // Show empty state UI if no data present
            if (groupedByDate.isEmpty() && groupedByCategory.isEmpty()) {
                // UI for no data, offering Add Expense and Add Income buttons with navigation
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        stringResource(R.string.no_data).ExpenseText(textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(size40))
                        Row(horizontalArrangement = Arrangement.SpaceBetween) {
                            AddExpenseButton(Modifier.clickable {
                                navController.navigate("$ADD_EXPENSE_SCREEN/true")
                            })
                            Spacer(modifier = Modifier.width(size10))
                            AddExpenseButton(Modifier.clickable {
                                navController.navigate("$ADD_EXPENSE_SCREEN/false")
                            }, isIncome = false)
                        }
                    }
                }
            }

            // Show category totals card list if data present
            if (groupedByCategory.isNotEmpty()) {
                SectionTitle(title = stringResource(R.string.category_totals))
                CardList(groupedByCategory)
            }

            // Show daily totals card list and line chart if data present
            if (groupedByDate.isNotEmpty()) {
                SectionTitle(title = stringResource(R.string.daily_totals))
                CardList(groupedByDate)
                Spacer(Modifier.height(size16))
                SectionTitle(title = stringResource(R.string.chart))
                LineChartWithPoints(
                    data = groupedByDate, modifier = Modifier
                        .fillMaxWidth()
                        .height(size400)
                        .padding(size30)
                )
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = size8),
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun AddExpenseButton(modifier: Modifier = Modifier, isIncome: Boolean = true) {
    val title =
        if (isIncome) stringResource(R.string.add_income) else stringResource(R.string.add_expense)
    val icon =
        if (isIncome) painterResource(R.drawable.ic_income) else painterResource(R.drawable.ic_expense)
    Row(modifier
        .border(BorderStroke(size2, color = Zinc), shape = CircleShape)
        .padding(size10)) {
        Icon(
            painter = icon,
            contentDescription = stringResource(R.string.empty),
            tint = Zinc
        )
        Spacer(modifier = Modifier.width(size10))
        title.ExpenseText()
    }
}

@Composable
fun CardList(
    groupedData: Map<String, List<ExpenseSummary>>,
) {
    groupedData.forEach { (label, items) ->
        val total = items.sumOf { it.totalAmount }
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = size4),
            shape = RoundedCornerShape(size12)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(size16),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                label.ExpenseText(
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "₹${"%.2f".format(total)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


