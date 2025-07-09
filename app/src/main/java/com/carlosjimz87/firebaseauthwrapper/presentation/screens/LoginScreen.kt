package com.carlosjimz87.firebaseauthwrapper.presentation.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.carlosjimz87.auth.Constants
import com.carlosjimz87.auth.domain.model.AuthUiEvent
import com.carlosjimz87.auth.presentation.AuthViewModel

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onAuthSuccess: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var selectedToken by remember { mutableStateOf(Constants.SUCCESS_ID_TOKEN) }
    var tokenMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(state.success) {
        if (state.success) onAuthSuccess()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = state.form.email,
            onValueChange = { viewModel.onEvent(AuthUiEvent.EmailChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("EmailField"),
            label = { Text("Email") }
        )

        OutlinedTextField(
            value = state.form.password,
            onValueChange = { viewModel.onEvent(AuthUiEvent.PasswordChanged(it)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("PasswordField"),
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = { viewModel.onEvent(AuthUiEvent.SubmitLogin) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("SignInButton"),
            enabled = !state.isLoading
        ) {
            Text("Sign In")
        }


        Box {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { tokenMenuExpanded = true },
                    modifier = Modifier.testTag("SelectTokenButton")
                ) {
                    Text("Select Google Token")
                }

                if (selectedToken.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier.testTag("SelectedTokenBadge")
                    ) {
                        Text(
                            text = selectedToken,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            DropdownMenu(
                expanded = tokenMenuExpanded,
                onDismissRequest = { tokenMenuExpanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Valid Token") },
                    onClick = {
                        selectedToken = Constants.SUCCESS_ID_TOKEN
                        tokenMenuExpanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Invalid Token") },
                    onClick = {
                        selectedToken = Constants.FAILURE_ID_TOKEN
                        tokenMenuExpanded = false
                    }
                )
            }
        }

        Button(
            onClick = { viewModel.onEvent(AuthUiEvent.GoogleLogin(selectedToken)) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("GoogleSignInButton"),
            enabled = !state.isLoading
        ) {
            Text("Sign In with Google")
        }

        if (state.isLoading) {
            CircularProgressIndicator(
                Modifier
                    .align(Alignment.CenterHorizontally)
                    .testTag("LoadingIndicator")
            )
        }

        state.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.testTag("ErrorText")
            )
        }
    }
}