package com.carlosjimz87.firebaseauthwrapper.scenario

import com.carlosjimz87.auth.domain.model.UiState
import com.carlosjimz87.auth.domain.repo.AuthRepository
import com.carlosjimz87.auth.presentation.AuthViewModel

class TestAuthViewModel(repo: AuthRepository) : AuthViewModel(repo) {
   fun reset() {
        super._uiState.value = UiState()
    }
}