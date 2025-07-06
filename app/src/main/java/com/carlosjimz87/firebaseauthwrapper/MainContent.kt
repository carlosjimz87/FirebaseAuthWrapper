package com.carlosjimz87.firebaseauthwrapper

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag

@Composable
fun MainContent() {
    Text("Welcome to the app!", Modifier.testTag("MainScreen"))
}