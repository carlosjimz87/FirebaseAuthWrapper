package com.carlosjimz87.firebaseauthwrapper.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.carlosjimz87.firebaseauthwrapper.presentation.composables.AuthFlow
import com.carlosjimz87.firebaseauthwrapper.presentation.screens.MainScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var isLoggedIn by remember { mutableStateOf(false) }

            if (isLoggedIn) {
                MainScreen(onLogout = { isLoggedIn = false })
            } else {
                AuthFlow(onAuthSuccess = { isLoggedIn = true })
            }
        }
    }
}