package com.carlosjimz87.firebaseauthwrapper.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.carlosjimz87.auth.presentation.AuthViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    viewModel: AuthViewModel = koinViewModel(),
    onLogout: () -> Unit) {
    Column(modifier = Modifier.testTag("MainScreen")) {
        Text("Welcome to the app!")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                viewModel.signOut()
                onLogout()
            },
            modifier = Modifier.testTag("LogoutButton")
        ) {
            Text("Logout")
        }
    }
}