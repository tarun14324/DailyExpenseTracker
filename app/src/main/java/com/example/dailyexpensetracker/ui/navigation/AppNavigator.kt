package com.example.dailyexpensetracker.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.dailyexpensetracker.ui.components.BottomNavigationBar
import com.example.dailyexpensetracker.ui.components.MultiFloatingActionButton
import com.example.dailyexpensetracker.ui.navigation.NavRouts.ADD_EXPENSE_SCREEN
import com.example.dailyexpensetracker.ui.navigation.NavRouts.ADD_EXPENSE_SCREEN_ROUTE
import com.example.dailyexpensetracker.ui.navigation.NavRouts.ALL_TRANSACTIONS
import com.example.dailyexpensetracker.ui.navigation.NavRouts.ARG_IS_INCOME
import com.example.dailyexpensetracker.ui.navigation.NavRouts.HOME_SCREEN
import com.example.dailyexpensetracker.ui.navigation.NavRouts.LOGIN_SCREEN
import com.example.dailyexpensetracker.ui.navigation.NavRouts.REPORT_SCREEN
import com.example.dailyexpensetracker.ui.navigation.NavRouts.PROFILE_SCREEN
import com.example.dailyexpensetracker.ui.navigation.NavRouts.SIGNUP_SCREEN
import com.example.dailyexpensetracker.ui.screens.AddExpenseScreen
import com.example.dailyexpensetracker.ui.screens.AllTransactionsScreen
import com.example.dailyexpensetracker.ui.screens.HomeScreen
import com.example.dailyexpensetracker.ui.screens.LoginScreen
import com.example.dailyexpensetracker.ui.screens.ReportScreen
import com.example.dailyexpensetracker.ui.screens.ProfileScreen
import com.example.dailyexpensetracker.ui.screens.SignupScreen
import com.example.dailyexpensetracker.ui.viewmodel.AuthViewModel
import com.example.dailyexpensetracker.core.states.HomeUiEvent

@Composable
fun AppNavigator(modifier: Modifier) {
    val navController = rememberNavController()
    val viewModel: AuthViewModel = hiltViewModel()

    // Observe current navigation back stack entry to get current route
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Screens where bottom bar should be shown
    val bottomBarScreens = listOf(HOME_SCREEN, REPORT_SCREEN, PROFILE_SCREEN)

    // Observe logged-in status from ViewModel
    val isLoggedIn = viewModel.isLoggedIn

    // Show loading indicator while login status is unknown
    if (isLoggedIn == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Listen to navigation events from ViewModel and navigate accordingly
    LaunchedEffect(Unit) {
        viewModel.navigationEvent.collect { event ->
            when (event) {
                HomeUiEvent.OnSeeAllClicked -> navController.navigate(ALL_TRANSACTIONS)
                HomeUiEvent.OnAddIncomeClicked -> navController.navigate("$ADD_EXPENSE_SCREEN/true")
                HomeUiEvent.OnAddExpenseClicked -> navController.navigate("$ADD_EXPENSE_SCREEN/false")
            }
        }
    }

    // Decide start destination based on login status
    val startDestination = if (isLoggedIn) HOME_SCREEN else LOGIN_SCREEN

    // State to hide/show floating action button (FAB)
    var hideFab by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            // Show FAB only on HOME_SCREEN and if not hidden by child composable
            if (currentRoute in listOf(HOME_SCREEN) && !hideFab) {
                MultiFloatingActionButton(
                    modifier = Modifier,
                    onAddExpenseClicked = { viewModel.sendEvent(HomeUiEvent.OnAddExpenseClicked) },
                    onAddIncomeClicked = { viewModel.sendEvent(HomeUiEvent.OnAddIncomeClicked) }
                )
            }
        },
        bottomBar = {
            // Show bottom navigation bar on specified screens
            if (currentRoute in bottomBarScreens) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        // Navigation host to manage composable destinations
        NavHost(
            navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            // Login screen with success callback navigating to home screen, popping login from backstack
            composable(LOGIN_SCREEN) {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = {
                        navController.navigate(HOME_SCREEN) {
                            popUpTo(LOGIN_SCREEN) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onSignupClick = { navController.navigate(SIGNUP_SCREEN) }
                )
            }

            // Signup screen with callback to pop backstack to previous screen
            composable(SIGNUP_SCREEN) {
                SignupScreen(
                    viewModel = viewModel,
                    onButtonClicked = { navController.popBackStack() }
                )
            }

            // Home screen with callback to toggle FAB visibility
            composable(HOME_SCREEN) {
                HomeScreen(navController) { hideFab = it }
            }

            composable(REPORT_SCREEN) {
                ReportScreen(navController)
            }

            composable(ALL_TRANSACTIONS) {
                AllTransactionsScreen(navController)
            }

            // Profile screen with navigation callback routing to appropriate screens
            composable(PROFILE_SCREEN) {
                ProfileScreen { route ->
                    when (route) {
                        REPORT_SCREEN -> navController.navigate(REPORT_SCREEN)
                        LOGIN_SCREEN -> navController.navigate(LOGIN_SCREEN) {
                            popUpTo(HOME_SCREEN) { inclusive = true }
                            launchSingleTop = true
                        }
                        ALL_TRANSACTIONS -> navController.navigate(ALL_TRANSACTIONS)
                    }
                }
            }

            // AddExpenseScreen with boolean argument determining income/expense type
            composable(
                route = ADD_EXPENSE_SCREEN_ROUTE,
                arguments = listOf(navArgument(ARG_IS_INCOME) { type = NavType.BoolType }),
            ) { backStackEntry ->
                val isIncome = backStackEntry.arguments?.getBoolean(ARG_IS_INCOME) ?: true
                AddExpenseScreen(isIncome = isIncome, navController)
            }
        }
    }
}