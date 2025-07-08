package com.carlosjimz87.firebaseauthwrapper.presentation.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun MainScreen() {
    Text("Welcome to the app!", Modifier.testTag("MainScreen"))
}