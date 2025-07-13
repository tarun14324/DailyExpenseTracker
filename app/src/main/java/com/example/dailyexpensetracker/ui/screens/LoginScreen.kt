package com.example.dailyexpensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.dailyexpensetracker.R
import com.example.dailyexpensetracker.core.extensions.ExpenseText
import com.example.dailyexpensetracker.core.extensions.toast
import com.example.dailyexpensetracker.core.extensions.verticalGradientBackground
import com.example.dailyexpensetracker.core.states.AuthState
import com.example.dailyexpensetracker.domain.model.User
import com.example.dailyexpensetracker.ui.components.AppOutlinedTextField
import com.example.dailyexpensetracker.ui.theme.size16
import com.example.dailyexpensetracker.ui.theme.size2
import com.example.dailyexpensetracker.ui.theme.size20
import com.example.dailyexpensetracker.ui.theme.size8
import com.example.dailyexpensetracker.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,     // Called after successful login
    onSignupClick: () -> Unit       // Called when user wants to sign up
) {
    val context = LocalContext.current

    // Local states to hold user input
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) } // Toggle visibility for password field

    val loginState = viewModel.loginState

    // Side-effect to react to login success or error
    LaunchedEffect(loginState) {
        if (loginState is AuthState.Success) {
            onLoginSuccess()          // Navigate to next screen
            viewModel.resetState()    // Reset login state in ViewModel
        } else if (loginState is AuthState.Error) {
            context.toast(loginState.message)  // Show error message
            viewModel.resetState()
        }
    }

    // Full screen container with vertical gradient and centered content
    Column(
        Modifier
            .fillMaxSize()
            .verticalGradientBackground(),  // Custom background
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Card-like form container
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .shadow(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        ) {
            // Title
            stringResource(R.string.login).ExpenseText(
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(size16))

            // Username input
            AppOutlinedTextField(
                value = username,
                onValueChange = { username = it },
                placeholder = stringResource(R.string.username)
            )

            Spacer(Modifier.height(size8))

            // Password input with visibility toggle
            AppOutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = stringResource(R.string.password),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else
                        Icons.Filled.VisibilityOff

                    val description = if (passwordVisible)
                        stringResource(R.string.hide_password)
                    else
                        stringResource(R.string.show_password)

                    Icon(
                        imageVector = image,
                        contentDescription = description,
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible } // Toggle visibility
                    )
                }
            )

            Spacer(Modifier.height(size16))

            // Login button
            Button(
                onClick = {
                    // Basic validation before login
                    if (username.length < 4) {
                        context.toast("Username must be at least 4 characters")
                        return@Button
                    }
                    if (password.length < 4) {
                        context.toast("Password must be at least 4 characters")
                        return@Button
                    }
                    viewModel.login(User(username, password)) // Trigger login event
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                // Show loading spinner while logging in
                if (loginState is AuthState.Loading) {
                    CircularProgressIndicator(
                        Modifier.size(size20),
                        color = Color.White,
                        strokeWidth = size2
                    )
                } else {
                    Text(stringResource(R.string.login))
                }
            }

            Spacer(Modifier.height(8.dp))

            // Navigate to sign-up screen
            TextButton(onClick = onSignupClick, modifier = Modifier.align(Alignment.End)) {
                Text(stringResource(R.string.new_user_sign_up_cta))
            }
        }
    }
}
