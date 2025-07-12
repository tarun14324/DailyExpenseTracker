package com.example.dailyexpensetracker.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.dailyexpensetracker.ui.navigation.NavRouts.home
import com.example.dailyexpensetracker.ui.navigation.NavRouts.login
import com.example.dailyexpensetracker.ui.navigation.NavRouts.signup
import com.example.dailyexpensetracker.ui.screens.LoginScreen
import com.example.dailyexpensetracker.ui.screens.SignupScreen
import com.example.dailyexpensetracker.ui.viewmodel.AuthViewModel

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    val viewModel: AuthViewModel = hiltViewModel()

    val isLoggedIn = viewModel.isLoggedIn

    if (isLoggedIn == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val startDestination = if (isLoggedIn) home else login

    NavHost(navController, startDestination = startDestination) {
        composable(login) {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = { navController.navigate(home) },
                onSignupClick = { navController.navigate(signup) }
            )
        }

        composable(signup) {
            SignupScreen(
                viewModel = viewModel,
                onSignupSuccess = { navController.popBackStack() }
            )
        }

        composable(home) {
            Text(home)
        }
    }
}