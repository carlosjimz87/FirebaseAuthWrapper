package com.carlosjimz87.firebaseauthwrapper.di

import com.carlosjimz87.auth.Constants
import com.carlosjimz87.auth.domain.model.AuthUiEvent
import com.carlosjimz87.auth.domain.model.AuthUiState
import com.carlosjimz87.auth.domain.repo.AuthRepository
import com.carlosjimz87.auth.presentation.AuthViewModel
import kotlinx.coroutines.flow.update

class TestAuthViewModel(repo: AuthRepository) : AuthViewModel(repo) {

    override fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.GoogleLogin -> {
                println("🟡 Event: GoogleLogin with token = ${event.idToken}")

                _uiState.update {
                    println("🔄 Updating state: isLoading = true")
                    it.copy(isLoading = true, error = null)
                }

                if (event.idToken == Constants.SUCCESS_ID_TOKEN) {
                    _uiState.update {
                        println("✅ Google login successful. Final state: success = true")
                        it.copy(success = true, isLoading = false)
                    }
                } else {
                    _uiState.update {
                        println("❌ Google login failed. Final state: error = Google login failed")
                        it.copy(error = "Google login failed", isLoading = false)
                    }
                }
            }

            is AuthUiEvent.SubmitLogin -> {
                println("🟡 Event: SubmitLogin with email = ${uiState.value.form.email} and password = ${uiState.value.form.password}")

                if (uiState.value.form.email == Constants.SUCCESS_EMAIL &&
                    uiState.value.form.password == Constants.SUCCESS_PASSWORD
                ) {
                    _uiState.update {
                        println("✅ Login successful. Final state: success = true")
                        it.copy(success = true, isLoading = false)
                    }
                } else {
                    _uiState.update {
                        println("❌ Login failed. Final state: error = Login failed")
                        it.copy(error = "Login failed", isLoading = false)
                    }
                }
            }

            else -> {
                println("📨 Event delegated to base ViewModel: ${event::class.simpleName}")
                super.onEvent(event)
            }
        }

        println("🧪 State after event: ${_uiState.value}")
    }

    fun reset() {
        _uiState.value = AuthUiState()
    }
}