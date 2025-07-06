package com.carlosjimz87.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosjimz87.auth.domain.model.UiState
import com.carlosjimz87.auth.domain.repo.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel (private val authRepository: AuthRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun onEmailChanged(email: String) { _uiState.value = _uiState.value.copy(email = email) }
    fun onPasswordChanged(password: String) { _uiState.value = _uiState.value.copy(password = password) }

    fun login() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = authRepository.signInWithEmail(_uiState.value.email, _uiState.value.password)
            _uiState.value = if (result.isSuccess) _uiState.value.copy(isLoading = false, success = true)
            else _uiState.value.copy(isLoading = false, error = result.exceptionOrNull()?.localizedMessage)
        }
    }

    fun googleLogin(idToken: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val result = authRepository.signInWithGoogle(idToken)
            _uiState.value = if (result.isSuccess) _uiState.value.copy(isLoading = false, success = true)
            else _uiState.value.copy(isLoading = false, error = result.exceptionOrNull()?.localizedMessage)
        }
    }
}