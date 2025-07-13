package com.example.dailyexpensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dailyexpensetracker.ui.navigation.AppNavigator
import com.example.dailyexpensetracker.ui.theme.DailyExpenseTrackerAppTheme
import com.example.dailyexpensetracker.ui.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Enables drawing behind system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()

        setContent {
            // Retrieves HomeViewModel instance via Hilt
            val viewModel: HomeViewModel = hiltViewModel()

            // Observes the current theme setting from the view model
            val isDarkTheme = viewModel.isDarkTheme.collectAsState()

            // Applies the app theme based on the user's preference
            DailyExpenseTrackerAppTheme(isDarkTheme.value ?: false) {

                // Sets up the app's navigation structure
                Scaffold(
                    Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                ) { paddingValues ->
                    AppNavigator(
                        Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                }
            }
        }
    }
}
