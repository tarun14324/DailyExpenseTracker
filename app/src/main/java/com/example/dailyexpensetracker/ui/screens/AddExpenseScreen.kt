package com.example.dailyexpensetracker.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.dailyexpensetracker.R
import com.example.dailyexpensetracker.core.extensions.ExpenseText
import com.example.dailyexpensetracker.core.extensions.getTransactionCategories
import com.example.dailyexpensetracker.core.extensions.toast
import com.example.dailyexpensetracker.core.extensions.verticalGradientBackground
import com.example.dailyexpensetracker.core.utils.Utils
import com.example.dailyexpensetracker.data.local.ExpenseEntity
import com.example.dailyexpensetracker.core.states.InsertExpenseResult
import com.example.dailyexpensetracker.ui.components.AppOutlinedTextField
import com.example.dailyexpensetracker.ui.components.ExpenseDatePickerDialog
import com.example.dailyexpensetracker.ui.components.LottieConfettiAnimation
import com.example.dailyexpensetracker.ui.components.TitleComponent
import com.example.dailyexpensetracker.ui.navigation.NavRouts.HOME_SCREEN
import com.example.dailyexpensetracker.ui.theme.size16
import com.example.dailyexpensetracker.ui.theme.size64
import com.example.dailyexpensetracker.ui.theme.size8
import com.example.dailyexpensetracker.ui.theme.textSize14
import com.example.dailyexpensetracker.ui.theme.textSize20
import com.example.dailyexpensetracker.ui.viewmodel.AddExpenseViewModel
import kotlinx.coroutines.delay


@Composable
fun AddExpenseScreen(
    isIncome: Boolean,
    navController: NavController,
) {
    val viewModel: AddExpenseViewModel = hiltViewModel()

    val insertResult by viewModel.insertResult.collectAsState()
    val context = LocalContext.current

    val totalSpentToday = remember { mutableDoubleStateOf(0.0) }
    val showSuccessAnimation = remember { mutableStateOf(false) }

    LaunchedEffect(insertResult) {
        when (insertResult) {
            is InsertExpenseResult.Success -> {
                showSuccessAnimation.value = true
                delay(1000)
                navController.navigate(HOME_SCREEN) {
                    popUpTo(HOME_SCREEN) {
                        inclusive = false
                    }
                    launchSingleTop = true
                }
            }

            is InsertExpenseResult.ExpenseAlreadyExists -> {
                context.toast("This expense already exists. Please check the details and try again.")
            }

            is InsertExpenseResult.Error -> {
                context.toast("An error occurred. Please try again.")
            }

            null -> {}
        }

        viewModel.resetInsertResult()
    }


    Surface(modifier = Modifier.fillMaxSize()) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalGradientBackground()
        ) {
            val (nameRow, totalRow, card) = createRefs()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(size16)
                    .constrainAs(nameRow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back), contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .clickable {
                            navController.popBackStack()
                        })
                val type =
                    if (isIncome) stringResource(R.string.income) else stringResource(R.string.expense)
                type.ExpenseText(
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.White,
                    modifier = Modifier
                        .padding(size16)
                        .align(Alignment.Center)
                )
            }

            val todayTotalSpent =
                "Total Spent Today: ₹${"%.2f".format(totalSpentToday.doubleValue)}"
            todayTotalSpent.ExpenseText(
                modifier = Modifier.constrainAs(totalRow) {
                    top.linkTo(nameRow.bottom, margin = size16)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })
            ForegroundContent(modifier = Modifier.constrainAs(card) {
                top.linkTo(nameRow.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }, onAddExpenseClick = {
                viewModel.onAddExpenseClicked(it)
                totalSpentToday.doubleValue += it.amount
            }, isIncome = isIncome)

            if (showSuccessAnimation.value) {
                LottieConfettiAnimation()
            }
        }
    }
}


@Composable
fun ForegroundContent(
    modifier: Modifier,
    onAddExpenseClick: (model: ExpenseEntity) -> Unit,
    isIncome: Boolean
) {

    val context = LocalContext.current
    val category =
        if (isIncome) stringResource(R.string.income) else stringResource(R.string.expense)
    val name = remember {
        mutableStateOf("")
    }
    val incomeType = remember {
        mutableStateOf("")
    }
    val amount = remember {
        mutableStateOf("")
    }
    val date = remember {
        mutableLongStateOf(0L)
    }
    val dateDialogVisibility = remember {
        mutableStateOf(false)
    }

    val type = remember {
        mutableStateOf(if (isIncome) "Income" else "Expense")
    }
    val notes = remember { mutableStateOf("") }
    val receiptImageUri = remember { mutableStateOf<Uri?>(null) }

    val pickImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { receiptImageUri.value = uri }
        }
    )

    Column(
        modifier = modifier
            .padding(size16)
            .fillMaxWidth()
            .shadow(size16)
            .clip(
                RoundedCornerShape(size16)
            )
            .background(MaterialTheme.colorScheme.background)
            .padding(size16)
            .verticalScroll(rememberScrollState())
    ) {
        TitleComponent(stringResource(R.string.name))
        AppOutlinedTextField(
            value = name.value,
            textStyle = TextStyle(fontSize = textSize20),
            onValueChange = { newValue ->
                name.value = newValue
            },
            placeholder = stringResource(R.string.enter_name),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
        )

        Spacer(modifier = Modifier.size(size16))
        TitleComponent(title = stringResource(R.string.type))
        ExpenseDropDown(
            isIncome.getTransactionCategories(),
            onItemSelected = {
                incomeType.value = it
            })
        Spacer(modifier = Modifier.size(size16))
        AppOutlinedTextField(
            value = amount.value,
            textStyle = TextStyle(fontSize = textSize20),
            onValueChange = { newValue ->
                val filteredValue = newValue.filter { it.isDigit() || it == '.' }

                if (filteredValue.count { it == '.' } <= 1) {
                    amount.value = filteredValue
                }
            },
            placeholder = stringResource(R.string.enter_amount),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.ic_rupee),
                    contentDescription = null
                )
            }
        )
        Spacer(modifier = Modifier.size(size16))
        TitleComponent(stringResource(R.string.date))
        AppOutlinedTextField(
            value = if (date.longValue == 0L) "" else Utils.formatDateToHumanReadableForm(date.longValue),
            onValueChange = {},
            placeholder = stringResource(R.string.select_date),
            enabled = false,
            onClick = { dateDialogVisibility.value = true }
        )
        Spacer(modifier = Modifier.size(size16))
        TitleComponent(title = "Optional Notes")
        AppOutlinedTextField(
            value = notes.value,
            onValueChange = {
                if (it.length <= 100) notes.value = it
            },
            placeholder = stringResource(R.string.add_notes),
            maxLines = 3
        )

        Spacer(modifier = Modifier.size(size16))

        TitleComponent(title = stringResource(R.string.receipt_image))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                pickImage.launch("image/*")
            }) {
                val imageUrl =
                    if (receiptImageUri.value == null) stringResource(R.string.upload_image) else stringResource(
                        R.string.change_image
                    )
                imageUrl.ExpenseText()
            }
            Spacer(modifier = Modifier.size(size16))
            receiptImageUri.value?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Receipt Image",
                    modifier = Modifier.size(size64)
                )
            }
        }
        Spacer(modifier = Modifier.size(size16))
        Button(
            onClick = {
                val numericAmount = amount.value.toDoubleOrNull()
                when {
                    name.value.isBlank() -> {
                        context.toast("Please enter a valid name")
                    }

                    numericAmount == null || numericAmount <= 0.0 -> {
                        context.toast("Amount must be greater than 0")
                    }

                    numericAmount > 50000 -> {
                        context.toast("Amount must be less than 5000")
                    }

                    date.longValue == 0L -> {
                        context.toast("Please select a date")
                    }

                    else -> {
                        val model = ExpenseEntity(
                            id = null,
                            title = name.value.trim(),
                            amount = numericAmount,
                            date = Utils.formatDateToHumanReadableForm(date.longValue),
                            category = type.value,
                            note = notes.value,
                            imageUrl = receiptImageUri.value
                        )
                        onAddExpenseClick(model)
                    }
                }

            }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(size8)
        ) {
            category.ExpenseText(
                fontSize = textSize14,
            )
        }
    }
    if (dateDialogVisibility.value) {
        ExpenseDatePickerDialog(onDateSelected = {
            date.longValue = it
            dateDialogVisibility.value = false
        }, onDismiss = {
            dateDialogVisibility.value = false
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseDropDown(listOfItems: List<String>, onItemSelected: (item: String) -> Unit) {
    val expanded = remember {
        mutableStateOf(false)
    }
    val selectedItem = remember {
        mutableStateOf(listOfItems[0])
    }
    ExposedDropdownMenuBox(expanded = expanded.value, onExpandedChange = { expanded.value = it }) {
        OutlinedTextField(
            value = selectedItem.value,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryNotEditable),
            textStyle = TextStyle(fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.outline),
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value)
            },
            shape = RoundedCornerShape(size8),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                disabledPlaceholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f),
                focusedTextColor = MaterialTheme.colorScheme.onSurface,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                cursorColor = MaterialTheme.colorScheme.primary
            )
        )
        ExposedDropdownMenu(expanded = expanded.value, onDismissRequest = { }) {
            listOfItems.forEach {
                DropdownMenuItem(text = { it.ExpenseText() }, onClick = {
                    selectedItem.value = it
                    onItemSelected(selectedItem.value)
                    expanded.value = false
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddExpense() {
    AddExpenseScreen(true, rememberNavController())
}