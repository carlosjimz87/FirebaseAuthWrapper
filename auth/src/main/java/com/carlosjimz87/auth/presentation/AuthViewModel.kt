package com.carlosjimz87.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carlosjimz87.auth.domain.model.AuthUiEvent
import com.carlosjimz87.auth.domain.model.AuthUiState
import com.carlosjimz87.auth.domain.model.AuthUser
import com.carlosjimz87.auth.domain.repo.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

open class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    protected val _uiState = MutableStateFlow(AuthUiState())
    open val uiState: StateFlow<AuthUiState> = _uiState

    open fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.EmailChanged -> {
                _uiState.update {
                    it.copy(form = it.form.copy(email = event.value))
                }
            }

            is AuthUiEvent.PasswordChanged -> {
                _uiState.update {
                    it.copy(form = it.form.copy(password = event.value))
                }
            }

            is AuthUiEvent.SubmitLogin -> {
                val email = _uiState.value.form.email
                val password = _uiState.value.form.password
                authenticate {
                    authRepository.signInWithEmail(email, password)
                }
            }

            is AuthUiEvent.GoogleLogin -> {
                authenticate {
                    authRepository.signInWithGoogle(event.idToken)
                }
            }

            is AuthUiEvent.Logout -> {
                signOut()
            }
        }
    }

    protected open fun authenticate(authCall: suspend () -> Result<AuthUser>) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val result = authCall()

            _uiState.value = if (result.isSuccess) {
                onLoginSuccess(result.getOrNull())
            } else {
                onLoginFailure(result.exceptionOrNull())
            }
        }
    }

    protected open fun onLoginSuccess(user: AuthUser?): AuthUiState {
        return _uiState.value.copy(isLoading = false, success = true)
    }

    protected open fun onLoginFailure(exception: Throwable?): AuthUiState {
        return _uiState.value.copy(isLoading = false, error = exception?.localizedMessage)
    }

    open fun signOut() {
        viewModelScope.launch {
            authRepository.signOut()
            _uiState.value = AuthUiState()
        }
    }
}