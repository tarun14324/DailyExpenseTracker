package com.example.dailyexpensetracker.core.states

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.dailyexpensetracker.ui.navigation.NavRouts.HOME_SCREEN
import com.example.dailyexpensetracker.ui.navigation.NavRouts.PROFILE_SCREEN
import com.example.dailyexpensetracker.ui.navigation.NavRouts.REPORT_SCREEN

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Home : BottomNavItem(HOME_SCREEN, Icons.Default.Home, "Home")
    data object Reports : BottomNavItem(REPORT_SCREEN, Icons.Default.BarChart, "Reports")
    data object Profile : BottomNavItem(PROFILE_SCREEN, Icons.Default.AccountCircle, "Profile")
}