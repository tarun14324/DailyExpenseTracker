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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.dailyexpensetracker.R
import com.example.dailyexpensetracker.core.extensions.toast
import com.example.dailyexpensetracker.core.utils.AuthState
import com.example.dailyexpensetracker.domain.model.User
import com.example.dailyexpensetracker.ui.theme.size16
import com.example.dailyexpensetracker.ui.theme.size2
import com.example.dailyexpensetracker.ui.theme.size20
import com.example.dailyexpensetracker.ui.theme.size24
import com.example.dailyexpensetracker.ui.theme.size8
import com.example.dailyexpensetracker.ui.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit,
    onSignupClick: () -> Unit
) {
    val context = LocalContext.current
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val loginState = viewModel.loginState

    LaunchedEffect(loginState) {
        if (loginState is AuthState.Success) {
            onLoginSuccess()
            viewModel.resetState()
        } else if (loginState is AuthState.Error) {
            context.toast(loginState.message)
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(size24),
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(R.string.login), style = MaterialTheme.typography.headlineMedium)

        Spacer(Modifier.height(size16))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(R.string.username)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(size8))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else
                    Icons.Filled.VisibilityOff

                val description = if (passwordVisible) stringResource(R.string.hide_password) else stringResource(R.string.show_password)

                Icon(
                    imageVector = image,
                    contentDescription = description,
                    modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                )
            }
        )

        Spacer(Modifier.height(size16))

        Button(
            onClick = { viewModel.login(User(username, password)) },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (loginState is AuthState.Loading) {
                CircularProgressIndicator(Modifier.size(size20), color = Color.White, strokeWidth = size2)
            } else {
                Text(stringResource(R.string.login))
            }
        }

        Spacer(Modifier.height(8.dp))

        TextButton(onClick = onSignupClick, modifier = Modifier.align(Alignment.End)) {
            Text(stringResource(R.string.new_user_sign_up_cta))
        }
    }
}