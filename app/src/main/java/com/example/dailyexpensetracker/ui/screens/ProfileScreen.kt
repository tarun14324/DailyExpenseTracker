package com.example.dailyexpensetracker.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Dataset
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.dailyexpensetracker.R
import com.example.dailyexpensetracker.core.extensions.ExpenseText
import com.example.dailyexpensetracker.ui.navigation.NavRouts
import com.example.dailyexpensetracker.ui.theme.Zinc
import com.example.dailyexpensetracker.ui.theme.size100
import com.example.dailyexpensetracker.ui.theme.size12
import com.example.dailyexpensetracker.ui.theme.size16
import com.example.dailyexpensetracker.ui.theme.size20
import com.example.dailyexpensetracker.ui.theme.size24
import com.example.dailyexpensetracker.ui.theme.size26
import com.example.dailyexpensetracker.ui.theme.size4
import com.example.dailyexpensetracker.ui.theme.size48
import com.example.dailyexpensetracker.ui.theme.size64
import com.example.dailyexpensetracker.ui.theme.size8
import com.example.dailyexpensetracker.ui.theme.textSize64
import com.example.dailyexpensetracker.ui.viewmodel.ProfileViewModel


@Composable
fun ProfileScreen(
    onItemNavigation: (String) -> Unit,  // Callback for navigation actions with route string
) {
    val viewModel: ProfileViewModel = hiltViewModel()

    // State to control visibility of the Card for animation
    var isVisible by remember { mutableStateOf(true) }

    // Collect username from ViewModel as State
    val userName = viewModel.userName.collectAsState()

    // Extract first letter of username for profile icon, default to "?" if empty
    val userProfileIcon = userName.value.firstOrNull()?.uppercase() ?: "?"

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + scaleIn(initialScale = 0.9f),
        exit = fadeOut() + scaleOut(targetScale = 0.9f)
    ) {
        // Card container for the profile content
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = size16, vertical = size64),
            shape = RoundedCornerShape(size16),
            elevation = CardDefaults.cardElevation(defaultElevation = size8),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            // Column layout for content with spacing and center alignment
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(size24),
                verticalArrangement = Arrangement.spacedBy(size24),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                stringResource(R.string.profile).ExpenseText(
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(Modifier.height(size26))

                // Circular box showing the user profile initial icon
                Box(
                    modifier = Modifier
                        .size(size100)
                        .background(color = Zinc, shape = RoundedCornerShape(size20)),
                    contentAlignment = Alignment.Center
                ) {
                    userProfileIcon.ExpenseText(
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = textSize64
                        )
                    )
                }

                Spacer(Modifier.height(size26))

                // Logout button triggers hiding animation, calls ViewModel logout, and navigates to Login screen
                AnimatedButton(
                    text = stringResource(R.string.logout),
                    onClick = {
                        isVisible = false  // Trigger exit animation
                        viewModel.onLogoutClicked()  // Call logout logic in ViewModel
                        onItemNavigation(NavRouts.LOGIN_SCREEN)  // Navigate to Login screen
                    },
                    icon = Icons.AutoMirrored.Filled.ExitToApp,
                    containerColor = MaterialTheme.colorScheme.error
                )

                // Button to navigate to Reports screen
                AnimatedButton(
                    text = stringResource(R.string.go_to_reports),
                    onClick = { onItemNavigation(NavRouts.REPORT_SCREEN) },
                    icon = Icons.Default.Assessment
                )

                // Button to navigate to All Transactions screen
                AnimatedButton(
                    text = stringResource(R.string.all_transactions),
                    onClick = { onItemNavigation(NavRouts.ALL_TRANSACTIONS) },
                    icon = Icons.Default.Dataset
                )
            }
        }
    }
}

@Composable
fun AnimatedButton(
    text: String,
    onClick: () -> Unit,
    icon: ImageVector,
    containerColor: Color = MaterialTheme.colorScheme.primary,
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(size48)
            .graphicsLayer {
                shadowElevation = size4.toPx()
                shape = RoundedCornerShape(size12)
                clip = true
            },
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = size4)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.padding(end = 8.dp)
        )
        text.ExpenseText(
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
        )
    }
}
