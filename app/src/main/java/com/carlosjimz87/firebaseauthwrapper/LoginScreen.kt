package com.carlosjimz87.firebaseauthwrapper

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.carlosjimz87.auth.presentation.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onAuthSuccess: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    if (state.success) {
        LaunchedEffect(Unit) {
            onAuthSuccess()
        }
    }

    Column {
        TextField(
            value = state.email,
            onValueChange = viewModel::onEmailChanged,
            modifier = Modifier.testTag("EmailField")
        )
        TextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChanged,
            modifier = Modifier.testTag("PasswordField")
        )
        Button(
            onClick = { viewModel.emailLogin() },
            modifier = Modifier.testTag("SignInButton"),
            enabled = !state.isLoading
        ) {
            Text("Sign In")
        }

        if (state.isLoading) {
            CircularProgressIndicator(Modifier.testTag("LoadingIndicator"))
        }

        state.error?.let {
            Text(it, modifier = Modifier.testTag("ErrorText"))
        }
    }
}