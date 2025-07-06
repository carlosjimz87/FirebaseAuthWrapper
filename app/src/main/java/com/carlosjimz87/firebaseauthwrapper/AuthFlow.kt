package com.carlosjimz87.firebaseauthwrapper

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.carlosjimz87.auth.presentation.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthFlow() {
    val viewModel: AuthViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.success) {
        MainContent()
    } else {
        LoginScreen(viewModel = viewModel, onAuthSuccess = { })
    }
}