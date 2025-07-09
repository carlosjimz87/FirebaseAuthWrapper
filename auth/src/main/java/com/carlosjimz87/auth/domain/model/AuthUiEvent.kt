package com.carlosjimz87.auth.domain.model

sealed class AuthUiEvent {
    data class EmailChanged(val value: String) : AuthUiEvent()
    data class PasswordChanged(val value: String) : AuthUiEvent()
    object SubmitLogin : AuthUiEvent()
    data class GoogleLogin(val idToken: String) : AuthUiEvent()
    object Logout : AuthUiEvent()
}