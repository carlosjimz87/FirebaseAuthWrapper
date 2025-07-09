package com.carlosjimz87.auth.domain.model

data class AuthUiState(
    val form: AuthFormState = AuthFormState(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)