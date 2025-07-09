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
                println("🟡 Evento: GoogleLogin con token = ${event.idToken}")

                _uiState.update {
                    println("🔄 Actualizando estado: isLoading = true")
                    it.copy(isLoading = true, error = null)
                }

                if (event.idToken == Constants.SUCCESS_ID_TOKEN) {
                    _uiState.update {
                        println("✅ Login con Google exitoso. Estado final: success = true")
                        it.copy(success = true, isLoading = false)
                    }
                } else {
                    _uiState.update {
                        println("❌ Login con Google fallido. Estado final: error = Google login failed")
                        it.copy(error = "Google login failed", isLoading = false)
                    }
                }
            }

            is AuthUiEvent.SubmitLogin -> {
                println("🟡 Evento: SubmitLogin con email = ${uiState.value.form.email} y password = ${uiState.value.form.password}")

                if (uiState.value.form.email == Constants.SUCCESS_EMAIL &&
                    uiState.value.form.password == Constants.SUCCESS_PASSWORD
                ) {
                    _uiState.update {
                        println("✅ Login exitoso. Estado final: success = true")
                        it.copy(success = true, isLoading = false)
                    }
                } else {
                    _uiState.update {
                        println("❌ Login fallido. Estado final: error = Login failed")
                        it.copy(error = "Login failed", isLoading = false)
                    }
                }
            }

            else -> {
                println("📨 Evento delegado al ViewModel base: ${event::class.simpleName}")
                super.onEvent(event)
            }
        }

        println("🧪 Estado tras evento: ${_uiState.value}")
    }

    fun reset() {
        _uiState.value = AuthUiState()
    }
}