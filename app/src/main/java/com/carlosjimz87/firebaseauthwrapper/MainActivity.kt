package com.carlosjimz87.firebaseauthwrapper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimpleScreen()
        }
    }
}

@Composable
fun SimpleScreen() {
    MaterialTheme {
        Text("Hello, World!")
    }
}