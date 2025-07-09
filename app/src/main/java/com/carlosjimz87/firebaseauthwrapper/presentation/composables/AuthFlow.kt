package com.carlosjimz87.firebaseauthwrapper.presentation.composables

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.carlosjimz87.auth.presentation.AuthViewModel
import com.carlosjimz87.firebaseauthwrapper.presentation.screens.LoginScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AuthFlow(onAuthSuccess: () -> Unit) {
    val viewModel: AuthViewModel = koinViewModel()
    LoginScreen(viewModel = viewModel, onAuthSuccess = onAuthSuccess)
}