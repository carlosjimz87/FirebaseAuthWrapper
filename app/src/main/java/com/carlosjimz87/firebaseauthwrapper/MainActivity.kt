package com.carlosjimz87.firebaseauthwrapper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.carlosjimz87.auth.presentation.AuthViewModel
import org.koin.androidx.compose.koinViewModel


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    AuthFlow()
                }
            }
        }
    }
}

@Composable
fun AuthFlow() {
    val viewModel: AuthViewModel = koinViewModel()

    var isAuthenticated by remember { mutableStateOf(false) }

    if (isAuthenticated) {
        MainContent()
    } else {
        LoginScreen(
            viewModel = viewModel,
            onAuthSuccess = { isAuthenticated = true }
        )
    }
}