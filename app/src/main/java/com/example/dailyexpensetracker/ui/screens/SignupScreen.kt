package com.example.dailyexpensetracker.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.example.dailyexpensetracker.R
import com.example.dailyexpensetracker.core.extensions.toast
import com.example.dailyexpensetracker.core.states.AuthState
import com.example.dailyexpensetracker.domain.model.User
import com.example.dailyexpensetracker.ui.components.AppOutlinedTextField
import com.example.dailyexpensetracker.ui.theme.size16
import com.example.dailyexpensetracker.ui.theme.size2
import com.example.dailyexpensetracker.ui.theme.size20
import com.example.dailyexpensetracker.ui.theme.size24
import com.example.dailyexpensetracker.ui.theme.size8
import com.example.dailyexpensetracker.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.delay

@Composable
fun SignupScreen(
    viewModel: AuthViewModel,
    onButtonClicked: () -> Unit,
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Observe signup state from ViewModel
    val signupState = viewModel.signupState

    LaunchedEffect(signupState) {
        if (signupState is AuthState.Success) {
            // Show success toast, wait briefly, then trigger navigation callback
            context.toast(context.getString(R.string.sign_up_success))
            delay(1000)
            onButtonClicked()
            viewModel.resetState()
        } else if (signupState is AuthState.Error) {
            // Show error message toast and reset state
            context.toast(signupState.message)
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(size24),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            stringResource(R.string.sign_up),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(size16))
        AppOutlinedTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = stringResource(R.string.username)
        )

        Spacer(Modifier.height(size8))
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

                val description =
                    if (passwordVisible) stringResource(R.string.hide_password) else stringResource(
                        R.string.show_password
                    )

                Icon(
                    imageVector = image,
                    contentDescription = description,
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            }
        )

        Spacer(Modifier.height(size16))

        Button(
            onClick = {
                // Basic validation for minimum username and password length
                if (username.length < 4) {
                    context.toast("Username must be at least 4 characters")
                    return@Button
                }
                if (password.length < 4) {
                    context.toast("Password must be at least 4 characters")
                    return@Button
                }
                // Trigger signup process call through ViewModel
                viewModel.signup(User(username, password))
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (signupState is AuthState.Loading) {
                CircularProgressIndicator(
                    Modifier.size(size20),
                    color = Color.White,
                    strokeWidth = size2
                )
            } else {
                Text(stringResource(R.string.sign_up))
            }
        }
        // Button to navigate back to login screen

        Button(
            onClick = { onButtonClicked() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.login))
        }
    }
}