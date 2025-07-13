package com.example.dailyexpensetracker.ui.screens


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.dailyexpensetracker.R
import com.example.dailyexpensetracker.core.extensions.ExpenseText
import com.example.dailyexpensetracker.core.states.GroupType
import com.example.dailyexpensetracker.core.states.TransactionFilterState
import com.example.dailyexpensetracker.core.utils.Utils
import com.example.dailyexpensetracker.core.utils.Utils.getGreetingMessage
import com.example.dailyexpensetracker.data.local.ExpenseEntity
import com.example.dailyexpensetracker.ui.components.FilterControls
import com.example.dailyexpensetracker.ui.components.NoDataView
import com.example.dailyexpensetracker.ui.components.ThemeSwitcher
import com.example.dailyexpensetracker.ui.navigation.NavRouts.ALL_TRANSACTIONS
import com.example.dailyexpensetracker.ui.theme.Green
import com.example.dailyexpensetracker.ui.theme.LightGrey
import com.example.dailyexpensetracker.ui.theme.Purple80
import com.example.dailyexpensetracker.ui.theme.Red
import com.example.dailyexpensetracker.ui.theme.Zinc
import com.example.dailyexpensetracker.ui.theme.size1
import com.example.dailyexpensetracker.ui.theme.size100
import com.example.dailyexpensetracker.ui.theme.size12
import com.example.dailyexpensetracker.ui.theme.size16
import com.example.dailyexpensetracker.ui.theme.size2
import com.example.dailyexpensetracker.ui.theme.size200
import com.example.dailyexpensetracker.ui.theme.size24
import com.example.dailyexpensetracker.ui.theme.size250
import com.example.dailyexpensetracker.ui.theme.size4
import com.example.dailyexpensetracker.ui.theme.size40
import com.example.dailyexpensetracker.ui.theme.size64
import com.example.dailyexpensetracker.ui.theme.size8
import com.example.dailyexpensetracker.ui.theme.textSize14
import com.example.dailyexpensetracker.ui.theme.textSize16
import com.example.dailyexpensetracker.ui.viewmodel.HomeViewModel


@Composable
fun HomeScreen(
    navController: NavController,
    onScrolledToBottom: (Boolean) -> Unit //  Callback to inform parent when last item is fully visible
) {
    val viewModel: HomeViewModel = hiltViewModel()

    //  Collecting user data and theme preference from ViewModel using StateFlow
    val userName = viewModel.userName.collectAsState()
    val isDarkTheme = viewModel.isDarkTheme.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            // Constraint references
            val (nameRow, list, topBar, card, noData) = createRefs()

            //  Top AppBar Background (Header)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(size250)
                    .background(
                        color = Purple80,
                        shape = RoundedCornerShape(bottomEnd = size12, bottomStart = size12)
                    )
                    .constrainAs(topBar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            //  User greeting and profile info
            val userProfileName = userName.value

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = size64, start = size16, end = size16)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                // Greeting and name
                Column(modifier = Modifier.align(Alignment.CenterStart)) {
                    getGreetingMessage().ExpenseText(
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White
                    )
                    userProfileName.ExpenseText(
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                }

                // Theme switcher
                Row(modifier = Modifier.align(Alignment.CenterEnd)) {
                    ThemeSwitcher(
                        darkTheme = isDarkTheme.value ?: false
                    ) {
                        viewModel.toggleTheme(it)
                    }
                }
            }

            //  Fetching transactions from ViewModel
            val state = viewModel.expenses.collectAsState(initial = emptyList())
            val expense = viewModel.getTotalExpense(state.value)
            val income = viewModel.getTotalIncome(state.value)
            val balance = viewModel.getBalance(state.value)

            //  Summary card (Balance / Income / Expense)
            CardItem(
                modifier = Modifier.constrainAs(card) {
                    top.linkTo(nameRow.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                balance = balance, income = income, expense = expense
            )

            //  Show empty state if no transactions
            if (state.value.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.constrainAs(noData) {
                        top.linkTo(card.bottom, size100)
                        start.linkTo(card.start)
                        end.linkTo(card.end)
                    }
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add_task),
                        contentDescription = stringResource(R.string.empty),
                        modifier = Modifier.size(size40)
                    )
                    NoDataView(
                        modifier = Modifier,
                        "Looks empty! Add an expense or income to get started."
                    )
                }
            } else {
                //  Main transaction list - passes scroll callback upward
                TransactionList(
                    modifier = Modifier
                        .fillMaxWidth()
                        .constrainAs(list) {
                            top.linkTo(card.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            height = Dimension.fillToConstraints
                        },
                    list = state.value,
                    onSeeAllClicked = {
                        navController.navigate(ALL_TRANSACTIONS)
                    },
                    onScrolledToBottom = {
                        if (state.value.size > 3){
                            onScrolledToBottom(it)
                        }
                    } //  Used to hide FAB when last item is visible
                )
            }
        }
    }
}



@Composable
fun CardItem(
    modifier: Modifier,
    balance: String, income: String, expense: String
) {
    Column(
        modifier = modifier
            .padding(size16)
            .fillMaxWidth()
            .height(size200)
            .clip(RoundedCornerShape(size16))
            .background(Zinc)
            .padding(size16)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column {
                stringResource(R.string.total_balance).ExpenseText(
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.size(size8))
                balance.ExpenseText(
                    style = MaterialTheme.typography.headlineLarge, color = Color.White,
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            CardRowItem(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                title = "Income",
                amount = income,
                image = R.drawable.ic_income
            )
            Spacer(modifier = Modifier.size(size8))
            CardRowItem(
                modifier = Modifier
                    .align(Alignment.CenterEnd),
                title = "Expense",
                amount = expense,
                image = R.drawable.ic_expense
            )
        }

    }
}


@Composable
fun TransactionList(
    modifier: Modifier,
    list: List<ExpenseEntity>,
    title: String = stringResource(R.string.recent_transactions),
    onSeeAllClicked: () -> Unit,
    onScrolledToBottom: (Boolean) -> Unit
) {
    var filterState by remember { mutableStateOf(TransactionFilterState()) }
    val listState = rememberLazyListState()

    //  Detect if the LAST item is completely visible
    val isLastItemFullyVisible by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val visibleItems = layoutInfo.visibleItemsInfo
            val totalItems = layoutInfo.totalItemsCount

            if (totalItems == 0) return@derivedStateOf false
            val lastItem = visibleItems.lastOrNull() ?: return@derivedStateOf false

            val isLastItem = lastItem.index == totalItems - 1
            val isFullyVisible = lastItem.offset >= 0 &&
                    lastItem.offset + lastItem.size <= layoutInfo.viewportEndOffset

            isLastItem && isFullyVisible
        }
    }

    //  Trigger the callback when last item visibility changes
    LaunchedEffect(isLastItemFullyVisible) {
        onScrolledToBottom(isLastItemFullyVisible)
    }

    //  Apply filtering (by category or date)
    val filteredList = filterState.selectedDate?.let { date ->
        list.filter { item -> Utils.formatDayMonth(item.date) == date.toString() }
    } ?: list

    val groupedMap = when (filterState.groupType) {
        GroupType.DATE -> filteredList.groupBy { Utils.formatStringDateToMonthDayYear(it.date) }
        GroupType.CATEGORY -> filteredList.groupBy { it.category }
    }

    LazyColumn(modifier = modifier.padding(horizontal = size16), state = listState) {
        // Header and filters
        item {
            Column {
                HeaderSection(
                    title = title,
                    hasItems = list.isNotEmpty(),
                    onSeeAllClicked = onSeeAllClicked
                )
                FilterControls(
                    filterState = filterState,
                    onFilterChange = { filterState = it }
                )
                Spacer(modifier = Modifier.height(size12))
            }
        }

        // Grouped items (by date or category)
        groupedMap.forEach { (groupTitle, transactions) ->
            item {
                groupTitle.ExpenseText(
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.padding(vertical = size8)
                )
            }
            items(transactions, key = { it.id ?: 0 }) { item ->
                val amount = if (item.category == "Income") item.amount else item.amount * -1
                TransactionItem(
                    item = item,
                    amount = Utils.formatCurrency(amount),
                    date = Utils.formatStringDateToMonthDayYear(item.date),
                    color = if (item.category == "Income") Green else Red,
                    modifier = Modifier
                )
            }
        }
    }
}

@Composable
fun TransactionItem(
    item: ExpenseEntity,
    amount: String,
    date: String,
    color: Color,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = size8)
            .border(
                BorderStroke(size1, color = MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(size4)
            )
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.size(size8))
            ExpenseItemIcon(item)
            Spacer(modifier = Modifier.size(size8))
            Column {
                item.title.ExpenseText(fontSize = textSize16, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.size(size2))
                date.ExpenseText(fontSize = textSize14, color = LightGrey)
                Spacer(modifier = Modifier.size(size2))
                item.category.ExpenseText(fontSize = textSize14, color = LightGrey)
                Spacer(modifier = Modifier.size(size2))
                item.note.takeIf { it.isNotEmpty() }
                    ?.ExpenseText(fontSize = textSize14, color = LightGrey)
            }
        }
        amount.ExpenseText(
            fontSize = textSize16,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = size16),
            color = color
        )
    }
}

@Composable
fun HeaderSection(title: String, hasItems: Boolean, onSeeAllClicked: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth()) {
        title.ExpenseText(style = MaterialTheme.typography.titleLarge)

        if (hasItems) {
            stringResource(R.string.see_all).ExpenseText(
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable { onSeeAllClicked() }
            )
        }
    }
}

@Composable
fun ExpenseItemIcon(item: ExpenseEntity) {
    val imageUri = item.imageUrl

    if (imageUri != null && imageUri.toString().isNotBlank()) {
        val painter = rememberAsyncImagePainter(
            model = imageUri,
            contentScale = ContentScale.Crop
        )
        Image(
            painter = painter,
            contentDescription = "Expense Image",
            modifier = Modifier.size(size40)
        )
    } else if (item.category == "Income") {
        Icon(
            painter = painterResource(id = R.drawable.ic_income),
            contentDescription = "Income Icon",
            modifier = Modifier.size(size40)
        )
    } else {
        Icon(
            painter = painterResource(id = R.drawable.ic_expense),
            contentDescription = "Expense Icon",
            modifier = Modifier.size(size40)
        )
    }
}



@Composable
fun CardRowItem(modifier: Modifier, title: String, amount: String, image: Int) {
    Column(modifier = modifier) {
        Row {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.size(size8))
            title.ExpenseText(style = MaterialTheme.typography.bodyLarge, color = Color.White)
        }
        Spacer(modifier = Modifier.size(size4))
        amount.ExpenseText(style = MaterialTheme.typography.titleLarge, color = Color.White)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(rememberNavController()){}
}